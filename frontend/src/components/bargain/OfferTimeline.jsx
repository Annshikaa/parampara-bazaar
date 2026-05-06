import Badge from "../ui/Badge";

export default function OfferTimeline({ offers = [] }) {
  return (
    <div className="glass rounded-4xl p-5 shadow-soft">
      <div className="flex items-center justify-between">
        <div className="text-sm font-extrabold">Offer Timeline</div>
        <Badge tone="info">{offers.length} rounds</Badge>
      </div>

      <div className="mt-4 space-y-3">
        {offers.length === 0 && (
          <div className="text-sm text-black/55">No offers yet. Start the vibe ✨</div>
        )}

        {offers.map((o, idx) => (
          <div key={idx} className="rounded-3xl border border-black/10 bg-white/60 p-3">
            <div className="flex items-center justify-between">
              <div className="text-xs text-black/50">Round {idx + 1}</div>
              <div className="text-xs text-black/50">{o.time}</div>
            </div>
            <div className="mt-2 flex items-center justify-between">
              <div className="text-sm font-bold">You: ₹{Math.round(o.buyer)}</div>
              <div className="text-sm font-extrabold text-[rgb(var(--accent))]">
                Seller: ₹{Math.round(o.seller)}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}