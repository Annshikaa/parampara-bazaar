import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { CartProvider } from "./store/CartContext";
import { AppLayout } from "./components/layout/AppLayout";

// Page Views
import { Home } from "./pages/Home";
import { FamousRegions } from "./pages/FamousRegions";
import { FamousMarkets } from "./pages/FamousMarkets";
import { RegionDetails } from "./pages/RegionDetails";
import { MarketDetails } from "./pages/MarketDetails";
import { ProductDetails } from "./pages/ProductDetails";
import { BargainChat } from "./pages/BargainChat";
import { Cart } from "./pages/Cart";
import { ProfileDashboard } from "./pages/ProfileDashboard";

function App() {
  return (
    <CartProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<AppLayout />}>
            <Route index element={<Home />} />
            <Route path="famous-items" element={<FamousRegions />} />
            <Route path="famous-markets" element={<FamousMarkets />} />
            <Route path="places/:id" element={<RegionDetails />} />
            <Route path="markets/:id" element={<MarketDetails />} />
            <Route path="product/:id" element={<ProductDetails />} />
            <Route path="bargain/:sessionId" element={<BargainChat />} />
            <Route path="cart" element={<Cart />} />
            <Route path="profile" element={<ProfileDashboard />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </CartProvider>
  );
}

export default App;
