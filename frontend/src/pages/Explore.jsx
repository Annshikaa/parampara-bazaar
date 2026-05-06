import { useNavigate } from "react-router-dom";
import PageShell from "../components/layout/PageShell";
import Input from "../components/ui/Input";
import Button from "../components/ui/Button";
import Badge from "../components/ui/Badge";

const mock = [
  { id: 1, name: "Ajrakh Print Shirt", price: 1299, tag: "handmade" },
  { id: 2, name: "Banarasi Stole", price: 899, tag: "heritage" },
  { id: 3, name: "Brass Minimal Jhumka", price: 699, tag: "trend" },
  { id: 4, name: "Kalamkari Tote", price: 749, tag: "eco" },
];

export default function Explore() {
  const navigate = useNavigate();

  return (
    <PageShell>
      <div className="flex flex-wrap items-end justify-between gap-4">
        <div>
          <h1 className="text-3xl font-extrabold tracking-tight">Explore</h1>
          <p className="mt-1 text-sm text-black/60">
            Curated drops. Clean vibes. Bargain when you feel it.
          </p>
        </div>
        <div className="flex gap-2">
          <Badge tone="info">Trending</Badge>
          <Badge tone="neutral">Handmade</Badge>
          <Badge tone="success">Eco</Badge>
        </div>
      </div>

      <div className="mt-6 grid gap-3 md:grid-cols-12">
        <div className="md:col-span-9">
          <Input placeholder="Search: ajrakh, banarasi, tote…" />
        </div>
        <div className="md:col-span-3">
          <Button className="w-full">Filters</Button>
        </div>
      </div>

      <div className="mt-6 grid gap-4 md:grid-cols-4">
        {mock.map((p) => (
          <div
            key={p.id}
            className="group glass rounded-4xl p-5 shadow-soft transition hover:-translate-y-1 hover:shadow-lift"
          >
            <div className="flex items-center justify-between">
              <Badge tone="neutral">{p.tag}</Badge>
              <div className="text-xs text-black/45">#{p.id}</div>
            </div>

            <div className="mt-4 h-28 rounded-4xl bg-[linear-gradient(135deg,rgba(99,102,241,0.18),rgba(236,72,153,0.12),rgba(16,185,129,0.10))] border border-black/10" />

            <div className="mt-4 font-bold">{p.name}</div>
            <div className="mt-1 text-sm text-black/65">MRP ₹{p.price}</div>

            <div className="mt-4 flex gap-2">
              <Button
                variant="subtle"
                className="flex-1"
                onClick={() => navigate(`/product/${p.id}`)}
              >
                View
              </Button>
              <Button
                className="flex-1"
                onClick={() => navigate(`/product/${p.id}`)}
              >
                Bargain
              </Button>
            </div>
          </div>
        ))}
      </div>
    </PageShell>
  );
}