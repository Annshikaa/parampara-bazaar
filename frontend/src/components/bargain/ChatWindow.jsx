import { useEffect, useRef } from "react";
import ChatBubble from "./ChatBubble";
import TypingIndicator from "./TypingIndicator";

export default function ChatWindow({ messages, typing }) {
  const ref = useRef(null);

  useEffect(() => {
    if (!ref.current) return;
    ref.current.scrollTop = ref.current.scrollHeight;
  }, [messages, typing]);

  return (
    <div className="glass rounded-4xl shadow-soft border border-black/10">
      <div className="px-5 py-4 border-b border-black/10 flex items-center justify-between">
        <div>
          <div className="text-sm font-extrabold">Bargain Chat</div>
          <div className="text-xs text-black/55">Keep it short, keep it classy.</div>
        </div>
        <div className="text-xs text-black/50">Live</div>
      </div>

      <div ref={ref} className="h-[420px] overflow-y-auto px-4 py-4 space-y-3">
        {messages.map((m, idx) => (
          <ChatBubble key={idx} sender={m.sender} text={m.text} time={m.time} />
        ))}
        {typing && <TypingIndicator />}
      </div>
    </div>
  );
}