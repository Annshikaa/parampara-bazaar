export default function ChatBubble({ sender, text, time }) {
  const isUser = sender === "USER";
  return (
    <div className={`flex ${isUser ? "justify-end" : "justify-start"}`}>
      <div
        className={`max-w-[82%] rounded-4xl px-4 py-3 shadow-soft border ${
          isUser
            ? "bg-[linear-gradient(135deg,rgba(99,102,241,0.16),rgba(139,92,246,0.12))] border-black/10"
            : "bg-white/75 border-black/10"
        }`}
      >
        <div className="text-sm leading-relaxed text-black/90">{text}</div>
        <div className="mt-1 text-[11px] text-black/45 text-right">{time || ""}</div>
      </div>
    </div>
  );
}