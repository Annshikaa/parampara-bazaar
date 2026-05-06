import React, { useState, useEffect, useRef } from "react";
import { useParams, Link } from "react-router-dom";
import { Send, Handshake, ArrowLeft, Bot, User } from "lucide-react";
import http from "../api/http";
import { motion, AnimatePresence } from "framer-motion";

export const BargainChat = () => {
  const { sessionId } = useParams();
  const [product, setProduct] = useState(null);
  const [messages, setMessages] = useState([
    { id: 1, sender: "bot", text: "Namaste! I'm your local seller. How can I help you with this piece today?", isOffer: false }
  ]);
  const [inputText, setInputText] = useState("");
  const [offerPrice, setOfferPrice] = useState("");
  const [isTyping, setIsTyping] = useState(false);
  
  const endOfMessagesRef = useRef(null);

  useEffect(() => {
    fetch("/mock/products.json")
      .then(res => res.json())
      .then(data => {
        const found = data.find(p => p.id === sessionId);
        setProduct(found);
      });
  }, [sessionId]);

  useEffect(() => {
    endOfMessagesRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, isTyping]);

  const handleSend = async (e) => {
    e.preventDefault();
    if (!inputText.trim() && !offerPrice) return;

    const userMsgText = offerPrice ? `Offer: ₹${offerPrice} - ${inputText}` : inputText;
    const userMsg = { id: Date.now(), sender: "user", text: userMsgText, isOffer: !!offerPrice };
    
    setMessages(prev => [...prev, userMsg]);
    setIsTyping(true);
    
    const currentInput = inputText;
    const currentOffer = offerPrice;
    
    setInputText("");
    setOfferPrice("");

    try {
      const payload = {
        message: currentInput,
      };
      if (currentOffer) {
        payload.buyerOffer = Number(currentOffer);
      }

      const res = await http.post(`/chatbot/${sessionId}/chat`, payload);
      
      setMessages(prev => [...prev, {
        id: Date.now() + 1,
        sender: "bot",
        text: res.data.reply || "Let’s see what I can do for you...",
        isOffer: false
      }]);
    } catch (error) {
      console.error("Chat error", error);
    } finally {
      setIsTyping(false);
    }
  };

  if (!product) return <div className="text-center py-20 text-white/50">Waking up the seller...</div>;

  return (
    <div className="max-w-4xl mx-auto h-[calc(100vh-8rem)] flex flex-col glass-panel rounded-3xl overflow-hidden shadow-lift border border-white/10 mt-4 relative">
      
      {/* Header */}
      <div className="px-6 py-4 border-b border-white/10 bg-white/5 backdrop-blur-3xl flex items-center justify-between shrink-0 z-10">
        <div className="flex items-center gap-4">
          <Link to={`/product/${product.id}`} className="p-2 hover:bg-white/10 rounded-full transition-colors text-white/70 hover:text-white">
            <ArrowLeft className="w-5 h-5" />
          </Link>
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-gradient-to-br from-indigo-500 to-purple-500 flex items-center justify-center p-0.5">
              <div className="w-full h-full bg-[#1a1c29] rounded-full flex items-center justify-center">
                <Bot className="w-5 h-5 text-indigo-400" />
              </div>
            </div>
            <div>
              <h2 className="font-semibold text-lg leading-tight">Master Artisan AI</h2>
              <p className="text-xs text-emerald-400 flex items-center gap-1">
                <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse" />
                Online & Ready to Bargain
              </p>
            </div>
          </div>
        </div>

        {/* Product Snippet Context */}
        <div className="hidden md:flex items-center gap-3 bg-white/5 rounded-xl p-2 pr-4">
          <img src={product.image} className="w-10 h-10 rounded-lg object-cover" alt="" />
          <div>
            <p className="text-sm font-medium line-clamp-1 max-w-[150px]">{product.name}</p>
            <p className="text-xs text-white/60">₹{product.price}</p>
          </div>
        </div>
      </div>

      {/* Chat Area */}
      <div className="flex-1 overflow-y-auto p-6 space-y-6 scroll-smooth custom-scrollbar">
        <AnimatePresence>
          {messages.map((msg) => (
            <motion.div 
              key={msg.id}
              initial={{ opacity: 0, y: 10, scale: 0.95 }}
              animate={{ opacity: 1, y: 0, scale: 1 }}
              className={`flex w-full ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}
            >
              <div className={`flex gap-3 max-w-[80%] md:max-w-[70%] ${msg.sender === 'user' ? 'flex-row-reverse' : 'flex-row'}`}>
                
                <div className={`w-8 h-8 rounded-full flex items-center justify-center shrink-0 mt-auto ${msg.sender === 'user' ? 'bg-indigo-500/20 text-indigo-300' : 'bg-pink-500/20 text-pink-300'}`}>
                  {msg.sender === 'user' ? <User className="w-4 h-4"/> : <Bot className="w-4 h-4"/>}
                </div>
                
                <div className={`p-4 rounded-2xl ${
                  msg.sender === 'user' 
                    ? msg.isOffer 
                      ? 'bg-gradient-to-br from-indigo-600 to-purple-600 text-white shadow-glow rounded-br-sm'
                      : 'bg-indigo-500/20 text-white rounded-br-sm border border-indigo-500/30'
                    : 'bg-white/10 text-white/90 rounded-bl-sm border border-white/5 backdrop-blur-sm'
                }`}>
                  <p className="text-sm md:text-base leading-relaxed">{msg.text}</p>
                </div>
              </div>
            </motion.div>
          ))}
          {isTyping && (
            <motion.div 
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="flex justify-start w-full"
            >
              <div className="flex gap-3">
                <div className="w-8 h-8 rounded-full bg-pink-500/20 flex items-center justify-center mt-auto text-pink-300">
                  <Bot className="w-4 h-4" />
                </div>
                <div className="bg-white/10 rounded-2xl rounded-bl-sm p-4 flex gap-1 items-center h-[52px]">
                  <span className="w-2 h-2 bg-white/60 rounded-full animate-bounce [animation-delay:-0.3s]" />
                  <span className="w-2 h-2 bg-white/60 rounded-full animate-bounce [animation-delay:-0.15s]" />
                  <span className="w-2 h-2 bg-white/60 rounded-full animate-bounce" />
                </div>
              </div>
            </motion.div>
          )}
        </AnimatePresence>
        <div ref={endOfMessagesRef} />
      </div>

      {/* Input Area */}
      <form onSubmit={handleSend} className="p-4 bg-white/5 backdrop-blur-xl border-t border-white/10 shrink-0 z-10">
        <div className="flex items-end gap-2">
          
          <div className="flex-1 bg-black/40 border border-white/10 rounded-2xl p-2 flex flex-col gap-2 transition-colors focus-within:border-indigo-500/50">
            {/* Optional Offer Input */}
            <div className="flex items-center gap-2 px-2">
              <Handshake className="w-4 h-4 text-emerald-400" />
              <input 
                type="number" 
                placeholder="Make an offer (₹)" 
                value={offerPrice}
                onChange={(e) => setOfferPrice(e.target.value)}
                className="bg-transparent border-none outline-none text-emerald-400 placeholder:text-emerald-400/50 text-sm py-1 w-full"
              />
            </div>
            
            <div className="h-px w-full bg-white/5 hidden md:block" />
            
            <textarea
              value={inputText}
              onChange={(e) => setInputText(e.target.value)}
              placeholder="Type your message..."
              className="w-full bg-transparent resize-none border-none outline-none text-white placeholder:text-white/40 px-2 py-1 max-h-32 min-h-[44px] custom-scrollbar"
              rows={1}
              onKeyDown={(e) => {
                if (e.key === 'Enter' && !e.shiftKey) {
                  e.preventDefault();
                  handleSend(e);
                }
              }}
            />
          </div>

          <button 
            type="submit"
            disabled={(!inputText.trim() && !offerPrice) || isTyping}
            className="w-14 h-14 shrink-0 rounded-2xl bg-indigo-500 hover:bg-indigo-400 disabled:opacity-50 disabled:hover:bg-indigo-500 flex items-center justify-center text-white transition-colors shadow-glow"
          >
            <Send className="w-5 h-5 ml-1" />
          </button>
        </div>
      </form>
    </div>
  );
};
