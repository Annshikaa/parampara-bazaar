import Badge from "../ui/Badge";

export default function DealBanner({ status, price }) {
  if (!status) return null;

  const map = {
    ACCEPTED: { tone: "success", label: "Deal Accepted" },
    REJECTED: { tone: "danger", label: "Offer Rejected" },
    NEGOTIATING: { tone: "info", label: "Negotiating" },
  };

  const t = map[status] || map.NEGOTIATING;

  return (
    <div className="glass rounded-4xl p-4 shadow-soft flex items-center justify-between gap-3">
      <div className="flex items-center gap-2">
        <Badge tone={t.tone}>{t.label}</Badge>
        {price != null && (
          <span className="text-sm font-extrabold text-black/80">
            Current: ₹{Math.round(price)}
          </span>
        )}
      </div>
      <div className="text-xs text-black/50">Keep it polite, get better deals ✨</div>
    </div>
  );
}