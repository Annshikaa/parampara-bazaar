export default function TypingIndicator() {
  return (
    <div className="flex items-center gap-2 text-black/60">
      <div className="h-8 w-10 rounded-3xl bg-white/75 border border-black/10 shadow-soft flex items-center justify-center">
        <div className="flex gap-1">
          <span className="h-1.5 w-1.5 rounded-full bg-black/40 animate-bounce [animation-delay:-0.2s]" />
          <span className="h-1.5 w-1.5 rounded-full bg-black/40 animate-bounce [animation-delay:-0.1s]" />
          <span className="h-1.5 w-1.5 rounded-full bg-black/40 animate-bounce" />
        </div>
      </div>
      <span className="text-xs">Shopkeeper is typing…</span>
    </div>
  );
}