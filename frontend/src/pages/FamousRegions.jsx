import React, { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { Tilt3DCard } from "../components/ui/Tilt3DCard";

const REGIONS_API = "/api/discovery/regions";

const slugWithPrefix = (id) => `place-${id}`;

export const FamousRegions = () => {
  const [regions, setRegions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetch(REGIONS_API)
      .then((res) => res.json())
      .then((data) => setRegions(Array.isArray(data) ? data : []))
      .catch((err) => console.error("Failed to load regions", err))
      .finally(() => setLoading(false));
  }, []);

  const cards = useMemo(() => {
    return regions.map((r) => ({
      id: slugWithPrefix(r.id),
      name: r.city,
      subtitle: r.state,
      image: `/assets/places/${r.id}.jpg`,
      highlights: (r.items || []).slice(0, 2).map((i) => i.itemName),
    }));
  }, [regions]);

  if (loading)
    return (
      <div className="py-32 text-center text-white font-serif text-2xl">
        Loading Regions...
      </div>
    );

  return (
    <div className="space-y-16 pb-24">
      <div className="text-center py-16">
        <h1 className="text-5xl md:text-6xl font-serif font-bold text-white mb-6">
          Explore Regions
        </h1>
        <p className="text-gray-400 max-w-2xl mx-auto text-lg">
          Select a city to discover its internationally renowned handcrafted items, textiles, and heritage products.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8 max-w-7xl mx-auto px-4">
        {cards.map((region) => (
          <Link key={region.id} to={`/places/${region.id}`} className="block">
            <Tilt3DCard className="h-[350px] luxury-glass relative overflow-hidden group border-white/10 hover:border-[#D4AF37]/40 p-6 flex flex-col justify-end">
              <div className="absolute inset-0 z-0">
                <img
                  src={region.image}
                  alt={region.name}
                  loading="lazy"
                  className="w-full h-full object-cover opacity-40 group-hover:opacity-70 group-hover:scale-110 transition-all duration-700 mix-blend-screen"
                  onError={(e) => (e.currentTarget.style.display = "none")}
                />
                <div className="absolute inset-0 bg-gradient-to-t from-black via-black/40 to-transparent" />
              </div>

              <div className="relative z-10">
                <p className="text-[#D4AF37] font-semibold mb-2 uppercase tracking-[0.2em] text-xs">
                  {region.subtitle}
                </p>
                <h2 className="text-3xl font-serif font-bold text-white group-hover:text-white transition-colors">
                  {region.name}
                </h2>
                {region.highlights.length > 0 && (
                  <p className="text-gray-200 text-sm mt-2 line-clamp-2">
                    {region.highlights.join(" • ")}
                  </p>
                )}
              </div>
            </Tilt3DCard>
          </Link>
        ))}
      </div>
    </div>
  );
};
