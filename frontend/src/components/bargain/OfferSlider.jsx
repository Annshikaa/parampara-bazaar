import Input from "../ui/Input";
import Badge from "../ui/Badge";

export default function OfferSlider({
  min = 100,
  max = 2000,
  value,
  onChange,
  hint,
}) {
  return (
    <div className="glass rounded-4xl p-5 shadow-soft">
      <div className="flex items-center justify-between">
        <div className="text-sm font-extrabold">Your Offer</div>
        <Badge tone="neutral">₹{Math.round(value)}</Badge>
      </div>

      <div className="mt-4">
        <input
          type="range"
          min={min}
          max={max}
          value={value}
          onChange={(e) => onChange(Number(e.target.value))}
          className="w-full accent-[rgb(var(--accent))]"
        />
        <div className="mt-2 flex justify-between text-[11px] text-black/45">
          <span>₹{min}</span>
          <span>₹{max}</span>
        </div>
      </div>

      <div className="mt-4">
        <Input
          type="number"
          value={value}
          onChange={(e) => onChange(Number(e.target.value || 0))}
          placeholder="Type your offer"
        />
        {hint && <div className="mt-2 text-xs text-black/55">{hint}</div>}
      </div>
    </div>
  );
}