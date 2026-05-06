import { useMemo, useState } from "react";
import { useParams } from "react-router-dom";
import PageShell from "../components/layout/PageShell";
import Button from "../components/ui/Button";
import Input from "../components/ui/Input";
import Badge from "../components/ui/Badge";

import DealBanner from "../components/bargain/DealBanner";
import ChatWindow from "../components/bargain/ChatWindow";
import OfferSlider from "../components/bargain/OfferSlider";
import OfferTimeline from "../components/bargain/OfferTimeline";

import { sendBargainChat } from "../api/bargain.api";

function nowTime() {
  const d = new Date();
  return d.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
}

export default function Bargain() {
  const { sessionId } = useParams();

  // TEMP product (later from backend)
  const product = useMemo(
    () => ({
      name: "Ajrakh Print Shirt",
      mrp: 1299,
      min: 600,
      max: 1700,
    }),
    []
  );

  const [status, setStatus] = useState("NEGOTIATING"); // NEGOTIATING / ACCEPTED / REJECTED
  const [currentSellerOffer, setCurrentSellerOffer] = useState(product.mrp);

  const [typing, setTyping] = useState(false);
  const [sending, setSending] = useState(false);

  const [messages, setMessages] = useState([
    {
      sender: "BOT",
      text: "Namaste ji 😄 batao, kitne mein chahiye?",
      time: nowTime(),
    },
  ]);

  const [offer, setOffer] = useState(Math.round(product.mrp * 0.75));
  const [text, setText] = useState("");

  const [timeline, setTimeline] = useState([]);

  const send = async () => {
    // Don’t send if already accepted/rejected
    if (status === "ACCEPTED" || status === "REJECTED") return;

    const messageText = text.trim() || `My offer is ₹${offer}`;
    if (!messageText) return;

    // UI: add user msg
    setMessages((p) => [...p, { sender: "USER", text: messageText, time: nowTime() }]);
    setText("");

    // Call backend
    setTyping(true);
    setSending(true);

    try {
      const data = await sendBargainChat(sessionId, messageText, offer);

      // data: { reply, counterOffer, dealAccepted }
      const botReply = data?.reply || "Arre ji 😅 kuch issue aa gaya.";
      const counterOffer = data?.counterOffer;

      setMessages((p) => [...p, { sender: "BOT", text: botReply, time: nowTime() }]);

      // Update offer shown
      if (counterOffer !== null && counterOffer !== undefined) {
        setCurrentSellerOffer(counterOffer);

        // timeline entry only when we actually sent offer
        setTimeline((t) => [
          ...t,
          {
            buyer: offer,
            seller: counterOffer,
            time: nowTime(),
          },
        ]);
      }

      // Update status
      if (data?.dealAccepted === true) {
        setStatus("ACCEPTED");
      } else {
        setStatus("NEGOTIATING");
      }
    } catch (e) {
      // If backend sends 500 or CORS issue
      setMessages((p) => [
        ...p,
        {
          sender: "BOT",
          text: "Server error 😅 backend running hai? (CORS / sessionId check karo)",
          time: nowTime(),
        },
      ]);
    } finally {
      setTyping(false);
      setSending(false);
    }
  };

  return (
    <PageShell>
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-2xl font-extrabold tracking-tight">Bargain Room</h1>
            <Badge tone="info">Session #{sessionId}</Badge>
          </div>
          <div className="mt-1 text-sm text-black/60">
            {product.name} • MRP ₹{product.mrp}
          </div>
        </div>

        <div className="flex items-center gap-2">
          <Badge tone="neutral">Vibe Score: Polite Pro ✨</Badge>
          <Badge tone="success">Streak: 3🔥</Badge>
        </div>
      </div>

      <div className="mt-5">
        <DealBanner status={status} price={currentSellerOffer} />
      </div>

      <div className="mt-5 grid gap-5 md:grid-cols-12">
        {/* LEFT: Chat */}
        <div className="md:col-span-7">
          <ChatWindow messages={messages} typing={typing} />

          <div className="mt-4 glass rounded-4xl p-4 shadow-soft border border-black/10">
            <div className="flex flex-col gap-3 md:flex-row md:items-center">
              <Input
                value={text}
                onChange={(e) => setText(e.target.value)}
                placeholder="Type your message… (e.g., bhaiya thoda kam)"
                disabled={sending || status === "ACCEPTED" || status === "REJECTED"}
              />
              <Button
                onClick={send}
                className="md:w-[160px]"
                disabled={sending || status === "ACCEPTED" || status === "REJECTED"}
              >
                {sending ? "Sending..." : "Send →"}
              </Button>
            </div>
            <div className="mt-2 text-xs text-black/50">
              Tip: offer slider sets buyerOffer; message is optional.
            </div>
          </div>
        </div>

        {/* RIGHT: Offer + Timeline */}
        <div className="md:col-span-5 space-y-5">
          <OfferSlider
            min={product.min}
            max={product.max}
            value={offer}
            onChange={setOffer}
            hint="This offer will be sent as buyerOffer to backend."
          />

          <OfferTimeline offers={timeline} />

          <div className="glass rounded-4xl p-5 shadow-soft">
            <div className="text-sm font-extrabold">Bargain Stats</div>
            <div className="mt-4 grid grid-cols-2 gap-3">
              <div className="rounded-3xl border border-black/10 bg-white/60 p-4">
                <div className="text-xs text-black/50">You saved</div>
                <div className="mt-1 text-xl font-extrabold">
                  ₹{Math.max(0, product.mrp - Math.round(currentSellerOffer || 0))}
                </div>
              </div>
              <div className="rounded-3xl border border-black/10 bg-white/60 p-4">
                <div className="text-xs text-black/50">Rounds</div>
                <div className="mt-1 text-xl font-extrabold">{timeline.length}</div>
              </div>
            </div>
            <div className="mt-4 text-xs text-black/55">
              Phase 12D: load chat history from DB for this session.
            </div>
          </div>
        </div>
      </div>
    </PageShell>
  );
}