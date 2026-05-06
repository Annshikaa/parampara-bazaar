import React from "react";
import { Link } from "react-router-dom";
import { useCart } from "../store/CartContext";
import { Trash2, ShoppingBag, ArrowRight } from "lucide-react";
import { motion } from "framer-motion";

export const Cart = () => {
  const { cart, removeFromCart, cartTotal } = useCart();

  if (cart.length === 0) {
    return (
      <div className="max-w-4xl mx-auto py-20 flex flex-col items-center justify-center text-center">
        <div className="w-24 h-24 rounded-full bg-white/5 flex items-center justify-center mb-6">
          <ShoppingBag className="w-10 h-10 text-white/40" />
        </div>
        <h2 className="text-3xl font-bold mb-4">Your cart is empty</h2>
        <p className="text-white/60 mb-8 max-w-md">
          Looks like you haven't added any heritage items to your bag yet. Let's fix that.
        </p>
        <Link to="/" className="px-8 py-3 rounded-xl bg-white text-black font-semibold hover:bg-white/90 transition-colors">
          Start Exploring
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto py-8">
      <h1 className="text-4xl font-bold mb-8">Your Cart</h1>
      
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
        
        {/* Items List */}
        <div className="lg:col-span-2 flex flex-col gap-4">
          {cart.map((item, idx) => (
            <motion.div 
              key={`${item.id}-${item.size}`}
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: idx * 0.1 }}
              className="glass-panel p-4 rounded-3xl flex items-center gap-6 group"
            >
              <div className="w-24 h-24 md:w-32 md:h-32 shrink-0 rounded-2xl overflow-hidden">
                <img src={item.image} className="w-full h-full object-cover" alt={item.name} />
              </div>
              
              <div className="flex-1 flex flex-col justify-center">
                <div className="flex justify-between items-start">
                  <div>
                    <h3 className="text-lg md:text-xl font-bold text-white/90 mb-1">{item.name}</h3>
                    <p className="text-sm text-white/60">Size: {item.size}</p>
                  </div>
                  <button 
                    onClick={() => removeFromCart(item.id, item.size)}
                    className="p-2 rounded-full hover:bg-red-500/20 text-red-400 opacity-0 group-hover:opacity-100 transition-all"
                  >
                    <Trash2 className="w-5 h-5" />
                  </button>
                </div>
                
                <div className="flex items-center justify-between mt-4">
                  <div className="flex items-center gap-3 bg-white/5 rounded-full px-3 py-1 border border-white/10">
                    <span className="text-sm text-white/60">Qty:</span>
                    <span className="font-semibold text-white">{item.quantity}</span>
                  </div>
                  <p className="text-xl font-bold">₹{(item.price * item.quantity).toLocaleString()}</p>
                </div>
              </div>
            </motion.div>
          ))}
        </div>

        {/* Checkout Summary */}
        <div className="lg:col-span-1">
          <div className="glass-panel rounded-3xl p-6 sticky top-28">
            <h2 className="text-2xl font-bold mb-6">Order Summary</h2>
            
            <div className="space-y-4 text-sm font-medium text-white/70 mb-6">
              <div className="flex justify-between">
                <span>Subtotal</span>
                <span className="text-white">₹{cartTotal.toLocaleString()}</span>
              </div>
              <div className="flex justify-between">
                <span>Shipping</span>
                <span className="text-emerald-400">Free</span>
              </div>
              <div className="flex justify-between">
                <span>Taxes</span>
                <span className="text-white">Calculated at checkout</span>
              </div>
            </div>
            
            <div className="h-px w-full bg-white/10 mb-6" />
            
            <div className="flex justify-between items-center mb-8">
              <span className="text-lg font-medium text-white/90">Total</span>
              <span className="text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-indigo-400 to-pink-400">
                ₹{cartTotal.toLocaleString()}
              </span>
            </div>

            <button className="w-full py-4 rounded-xl flex items-center justify-center gap-2 bg-gradient-to-r from-indigo-500 to-purple-600 shadow-glow text-white font-bold text-lg hover:opacity-90 transition-opacity">
              Checkout <ArrowRight className="w-5 h-5" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
