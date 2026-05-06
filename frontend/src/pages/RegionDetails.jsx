import React, { useEffect, useMemo, useState } from "react";
import { useParams } from "react-router-dom";
import { ProductCard } from "../components/products/ProductCard";

const REGIONS_API = "/api/discovery/regions";

const normalizeRegionId = (raw) => (raw || "").replace(/^place-/, "");

const fallbackImage = (e) => {
  e.currentTarget.style.display = "none";
};

export const RegionDetails = () => {
  const { id } = useParams(); // e.g. "place-varanasi" or "varanasi"
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

  const { region, products } = useMemo(() => {
    const cleanId = normalizeRegionId(id);
    const found = regions.find((r) => r.id === cleanId);
    if (!found) return { region: null, products: [] };

    const image = `/assets/places/${found.id}.jpg`;
    const products = (found.items || []).map((item, idx) => ({
      id: `prod-${found.id}-${idx + 1}`,
      name: item.itemName,
      price: 1200 + (idx % 8) * 180,
      image,
      rating: 4.6,
      tag: item.category,
      description: item.description,
      placeId: `place-${found.id}`,
    }));

    return {
      region: {
        id: found.id,
        name: found.city,
        subtitle: found.state,
        image,
      },
      products,
    };
  }, [id, regions]);

  if (loading || !region)
    return (
      <div className="py-32 text-center text-white font-serif text-2xl">
        Loading Region...
      </div>
    );

  return (
    <div className="space-y-16 pb-32">
      <div className="relative rounded-3xl overflow-hidden h-[450px] flex items-center justify-center luxury-glass">
        <img
          src={region.image}
          alt={region.name}
          className="absolute inset-0 w-full h-full object-cover opacity-50"
          onError={fallbackImage}
        />
        <div className="absolute inset-0 bg-gradient-to-t from-[#0A0A0A] to-transparent" />
        <div className="relative z-10 text-center px-4 max-w-3xl mx-auto">
          <p className="text-[#D4AF37] font-bold mb-4 uppercase tracking-[0.3em] text-sm">
            {region.subtitle}
          </p>
          <h1 className="text-6xl md:text-8xl font-serif text-white mb-6 drop-shadow-2xl">
            {region.name}
          </h1>
          <p className="text-gray-300 text-lg sm:text-xl font-light leading-relaxed">
            Famous traditional items natively crafted in {region.name}
          </p>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4">
        <div className="flex justify-between items-end mb-12">
          <div>
            <h2 className="text-4xl font-serif text-white font-bold">
              Famous Items
            </h2>
            <p className="text-gray-400 mt-2 text-lg drop-shadow-md">
              Direct from the artisans of {region.name}
            </p>
          </div>
          <div className="hidden sm:inline-flex bg-[#D4AF37]/10 border border-[#D4AF37]/30 text-[#D4AF37] px-4 py-2 rounded-full font-bold text-sm tracking-widest uppercase">
            {products.length} Treasures
          </div>
        </div>

        {products.length > 0 ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
            {products.map((p) => (
              <ProductCard key={p.id} product={p} />
            ))}
          </div>
        ) : (
          <div className="text-center py-20 luxury-glass rounded-2xl max-w-2xl mx-auto">
            <p className="text-gray-400 font-serif text-xl">
              The artisans are arriving soon with new items.
            </p>
          </div>
        )}
      </div>
    </div>
  );
};
