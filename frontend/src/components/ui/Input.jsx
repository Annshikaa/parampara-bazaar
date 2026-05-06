export default function Input({ className = "", ...props }) {
  return (
    <input
      className={
        "w-full rounded-2xl border border-black/10 bg-white/80 px-4 py-2 text-sm outline-none transition " +
        "focus:border-[rgba(99,102,241,0.5)] focus:ring-4 focus:ring-[rgba(99,102,241,0.14)] " +
        className
      }
      {...props}
    />
  );
}