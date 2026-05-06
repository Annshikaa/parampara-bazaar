export default function Button({
  children,
  variant = "primary",
  className = "",
  ...props
}) {
  const base =
    "relative inline-flex items-center justify-center gap-2 rounded-2xl px-4 py-2 text-sm font-semibold transition active:scale-[0.98] disabled:opacity-60 disabled:cursor-not-allowed";

  const styles = {
    primary:
      "text-white shadow-soft hover:shadow-lift focus:outline-none focus:shadow-glow " +
      "bg-[linear-gradient(135deg,rgba(99,102,241,1),rgba(139,92,246,1))]",
    ghost:
      "bg-white/50 text-black/80 hover:bg-white/70 border border-black/10",
    subtle:
      "bg-black/5 text-black/80 hover:bg-black/10",
  };

  return (
    <button className={`${base} ${styles[variant]} ${className}`} {...props}>
      {/* tiny highlight */}
      <span className="pointer-events-none absolute inset-0 overflow-hidden rounded-2xl">
        <span className="absolute -left-1/2 top-0 h-full w-1/2 rotate-12 bg-white/20 blur-[2px] animate-shimmer" />
      </span>
      <span className="relative">{children}</span>
    </button>
  );
}