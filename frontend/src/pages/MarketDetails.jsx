import React, { useEffect, useMemo, useState } from "react";
import { useParams } from "react-router-dom";
import { ProductCard } from "../components/products/ProductCard";

const MARKETS_API = "/api/discovery/markets";
const REGIONS_API = "/api/discovery/regions"; // used to borrow famous items as sample products

const normalizeMarketId = (raw) => (raw || "").replace(/^market-/, "");

const slugImage = (id) => `/assets/markets/${id}.jpg`;

export const MarketDetails = () => {
  const { id } = useParams(); // e.g. "market-anjuna-flea-market" or "anjuna-flea-market"
  const [markets, setMarkets] = useState([]);
  const [regions, setRegions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    Promise.all([
      fetch(MARKETS_API).then((r) => r.json()),
      fetch(REGIONS_API).then((r) => r.json()),
    ])
      .then(([mkt, reg]) => {
        setMarkets(Array.isArray(mkt) ? mkt : []);
        setRegions(Array.isArray(reg) ? reg : []);
      })
      .catch((err) => console.error("Failed to load market details", err))
      .finally(() => setLoading(false));
  }, []);

  const { market, products } = useMemo(() => {
    const cleanId = normalizeMarketId(id);
    const found = markets.find((m) => m.id === cleanId);
    if (!found) return { market: null, products: [] };

    // pull any famous item from the matching city to show at least one product
    const cityMatch = regions.find(
      (r) => r.city?.toLowerCase() === found.city?.toLowerCase()
    );
    const items = cityMatch?.items || [];
    const first = items[0] || {
      itemName: `${found.name} Special`,
      category: "Featured",
      description: found.bestFor,
    };

    const sampleProduct = {
      id: `mprod-${found.id}-1`,
      name: first.itemName,
      price: 900 + (found.id.length % 7) * 150,
      image: slugImage(found.id),
      rating: 4.5,
      tag: first.category,
      description: first.description,
      marketId: `market-${found.id}`,
    };

    return {
      market: {
        ...found,
        image: slugImage(found.id),
      },
      products: [sampleProduct],
    };
  }, [id, markets, regions]);

  if (loading || !market)
    return (
      <div className="py-32 text-center text-ink font-serif text-2xl">
        Arriving at the market...
      </div>
    );

  return (
    <div className="space-y-16 pb-32">
      <div className="relative rounded-3xl overflow-hidden h-[320px] flex items-center justify-center">
        <div
          className="absolute inset-0"
          style={{ backgroundImage: "linear-gradient(135deg,#8B0000cc,#0f172a)" }}
        />
        <div className="absolute inset-0 bg-ink/40" />
        <div className="relative z-20 text-center px-4 max-w-2xl mx-auto">
          <p className="text-gold font-bold mb-4 uppercase tracking-[0.2em] text-sm">
            {market.city} • {market.region} • Bargaining {market.bargainingLevel}
          </p>
          <h1 className="text-5xl md:text-7xl font-serif text-white mb-6 drop-shadow-md">
            {market.name}
          </h1>
          <p className="text-white/90 text-lg sm:text-xl font-light leading-relaxed">
            {market.bestFor}
          </p>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4">
        <div className="flex items-center justify-between mb-12">
          <div>
            <h2 className="text-4xl font-serif text-ink font-bold">
              Heritage Items
            </h2>
            <p className="text-ink/60 mt-2 text-lg">
              Authentic crafts and textiles found here
            </p>
          </div>
          <div className="hidden sm:flex text-sm font-medium py-2 px-4 border border-sandstone rounded-full text-ink tracking-wide">
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
          <div className="text-center py-20 bg-white luxury-card rounded-2xl max-w-2xl mx-auto border-dashed border-2 border-sandstone">
            <p className="text-ink/60 font-serif text-xl">
              The artisans are arriving soon with new items.
            </p>
          </div>
        )}
      </div>
    </div>
  );
};
