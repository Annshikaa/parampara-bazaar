export default function Badge({ children, tone = "neutral" }) {
  const tones = {
    neutral: "bg-black/5 text-black/70 border-black/10",
    success: "bg-emerald-500/10 text-emerald-700 border-emerald-200",
    danger: "bg-rose-500/10 text-rose-700 border-rose-200",
    info: "bg-indigo-500/10 text-indigo-700 border-indigo-200",
  };

  return (
    <span className={`inline-flex items-center rounded-full border px-3 py-1 text-xs font-semibold ${tones[tone]}`}>
      {children}
    </span>
  );
}