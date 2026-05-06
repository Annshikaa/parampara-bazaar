import fs from 'fs';

const itemsCsv = fs.readFileSync('backend/src/main/resources/data/famous-items.csv', 'utf-8');
const marketsCsv = fs.readFileSync('backend/src/main/resources/data/market.csv', 'utf-8');

function parseCsv(csv) {
  const lines = csv.split(/\r?\n/).filter(l => l.trim().length > 0);
  const headers = lines[0].split(',').map(h => h.trim().replace(/^"|"$/g, ''));
  const data = [];
  
  for (let i = 1; i < lines.length; i++) {
    const row = [];
    let cur = "";
    let inQuotes = false;
    for (let char of lines[i]) {
      if (char === '"') inQuotes = !inQuotes;
      else if (char === ',' && !inQuotes) {
        row.push(cur.trim());
        cur = "";
      } else {
        cur += char;
      }
    }
    row.push(cur.trim());
    if (row.length === 0) continue;
    
    const obj = {};
    headers.forEach((h, idx) => {
      let val = row[idx] ? row[idx] : '';
      if (val.startsWith('"') && val.endsWith('"')) val = val.slice(1, -1);
      obj[h.replace(/\r/g, '')] = val.replace(/\r/g, '');
    });
    data.push(obj);
  }
  return data;
}

const itemsData = parseCsv(itemsCsv);
const marketsData = parseCsv(marketsCsv);

// 1. Gather all unique cities to form Regions (places.json)
const placesMap = new Map();

marketsData.forEach(m => {
  if (!m.City) return;
  const pId = 'place-' + m.City.toLowerCase().replace(/[^a-z0-9]/g, '-');
  if (!placesMap.has(pId)) {
    placesMap.set(pId, { id: pId, name: m.City, subtitle: m.Region, image: "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?q=80&w=800&auto=format&fit=crop", description: `Historic city of ${m.City}` });
  }
});

itemsData.forEach(item => {
  if (!item.place) return;
  const pId = 'place-' + item.place.toLowerCase().replace(/[^a-z0-9]/g, '-');
  if (!placesMap.has(pId)) {
    placesMap.set(pId, { id: pId, name: item.place, subtitle: item.state, image: "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?q=80&w=800&auto=format&fit=crop", description: `Historic city of ${item.place}` });
  }
});
fs.writeFileSync('frontend/public/mock/places.json', JSON.stringify(Array.from(placesMap.values()), null, 2));

// 2. Map all 220 Markets
const placeToMarkets = {};
const resolvedMarkets = marketsData.map((m, idx) => {
  const cityFields = [m.City, m.Region].filter(Boolean).join(', ');
  const pId = m.City ? 'place-' + m.City.toLowerCase().replace(/[^a-z0-9]/g, '-') : 'place-unknown';
  const mkt = {
    id: `market-${idx+1}`,
    name: m.Market_Name || `Market ${idx}`,
    subtitle: cityFields,
    placeId: pId,
    image: "https://images.unsplash.com/photo-1548013146-72479768bada?q=80&w=800&auto=format&fit=crop",
    description: `Specializes in: ${m.Best_For || 'Heritage goods'}`
  };
  
  if (!placeToMarkets[pId]) placeToMarkets[pId] = [];
  placeToMarkets[pId].push(mkt.id);
  return mkt;
});
fs.writeFileSync('frontend/public/mock/markets.json', JSON.stringify(resolvedMarkets, null, 2));

// 3. Map all 220 Items
const resolvedProducts = itemsData.map((item, idx) => {
  const pId = 'place-' + (item.place || 'unknown').toLowerCase().replace(/[^a-z0-9]/g, '-');
  
  let assignedMarket = null;
  if (placeToMarkets[pId] && placeToMarkets[pId].length > 0) {
    assignedMarket = placeToMarkets[pId][Math.floor(Math.random() * placeToMarkets[pId].length)];
  }

  // If a region has items but NO markets in the CSV, we must create a virtual market to hold them!
  // Otherwise strict Market logic hides them.
  if (!assignedMarket) {
     const virtualMarketId = `v-market-${pId}`;
     if (!placeToMarkets[pId]) placeToMarkets[pId] = [];
     if (!placeToMarkets[pId].includes(virtualMarketId)) {
        resolvedMarkets.push({
           id: virtualMarketId,
           name: `${item.place} Central Market`,
           subtitle: item.place,
           placeId: pId,
           image: "https://images.unsplash.com/photo-1548013146-72479768bada?q=80&w=800&auto=format&fit=crop",
           description: "Local artisan market."
        });
        placeToMarkets[pId].push(virtualMarketId);
     }
     assignedMarket = virtualMarketId;
  }

  return {
    id: `prod-${idx+1}`,
    name: item.itemName || `Item ${idx}`,
    price: Math.floor(Math.random() * 4000) + 500,
    image: "https://images.unsplash.com/photo-1605814515286-3ec4ceac05ee?q=80&w=800&auto=format&fit=crop",
    rating: (4.0 + Math.random()).toFixed(1),
    placeId: pId,
    marketId: assignedMarket,
    tag: item.category || 'Handicraft',
    description: item.description || ''
  };
});

fs.writeFileSync('frontend/public/mock/products.json', JSON.stringify(resolvedProducts, null, 2));
// Re-write markets to include Virtual markets if they were added
fs.writeFileSync('frontend/public/mock/markets.json', JSON.stringify(resolvedMarkets, null, 2));
console.log(`Generated ${placesMap.size} Regions, ${resolvedMarkets.length} Markets, ${resolvedProducts.length} Products`);
