import React, { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { Tilt3DCard } from "../components/ui/Tilt3DCard";

const MARKETS_API = "/api/discovery/markets";

const withPrefix = (id) => `market-${id}`;

export const FamousMarkets = () => {
  const [markets, setMarkets] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetch(MARKETS_API)
      .then((res) => res.json())
      .then((data) => setMarkets(Array.isArray(data) ? data : []))
      .catch((err) => console.error("Failed to load markets", err))
      .finally(() => setLoading(false));
  }, []);

  const cards = useMemo(
    () =>
      markets.map((m) => ({
        id: withPrefix(m.id), // backend id is slug(name)
        name: m.name,
        subtitle: `${m.city} · ${m.region}`,
        image: `/assets/markets/${m.id}.jpg`,
        description: m.bestFor,
      })),
    [markets]
  );

  if (loading)
    return (
      <div className="py-32 text-center text-white font-serif text-2xl">
        Loading Markets...
      </div>
    );

  return (
    <div className="space-y-16 pb-24">
      <div className="text-center py-16">
        <h1 className="text-5xl md:text-6xl font-serif font-bold text-white mb-6">
          Explore Markets
        </h1>
        <p className="text-gray-400 max-w-2xl mx-auto text-lg leading-relaxed">
          Navigate 220+ of the most chaotic, vibrant, and historic bazaars in India. Each housing unique regional specialties.
        </p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 max-w-[1400px] mx-auto px-4">
        {cards.map((market) => (
          <Link key={market.id} to={`/markets/${market.id}`} className="block">
            <Tilt3DCard className="h-[280px] luxury-glass relative overflow-hidden group border-white/10 hover:border-[#8B0000]/50 p-6 flex flex-col justify-end">
              <div className="absolute inset-0 z-0 bg-black/60">
                <img
                  src={market.image}
                  alt={market.name}
                  loading="lazy"
                  className="w-full h-full object-cover opacity-30 group-hover:opacity-70 group-hover:scale-110 transition-all duration-700"
                  onError={(e) => (e.currentTarget.style.display = "none")}
                />
                <div className="absolute inset-0 bg-gradient-to-t from-[#0A0A0A] via-[#0A0A0A]/40 to-transparent" />
              </div>

              <div className="relative z-10 text-left">
                <p className="text-[#D4AF37] font-semibold mb-2 uppercase tracking-[0.1em] text-[10px] line-clamp-1">
                  {market.subtitle}
                </p>
                <h3 className="text-2xl font-serif font-bold text-white group-hover:text-white transition-colors line-clamp-2 leading-tight">
                  {market.name}
                </h3>
                <p className="text-gray-200 text-xs mt-2 line-clamp-2">
                  {market.description}
                </p>
              </div>
            </Tilt3DCard>
          </Link>
        ))}
      </div>
    </div>
  );
};
