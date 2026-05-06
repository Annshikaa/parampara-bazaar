
import React, { useState } from "react";
import { User, Store, LogOut, Package, Heart, Tag, Image, Upload } from "lucide-react";
import { motion } from "framer-motion";

export const ProfileDashboard = () => {
  const [role, setRole] = useState("buyer"); // 'buyer' or 'vendor'

  return (
    <div className="min-h-screen pt-32 pb-24 px-4 max-w-7xl mx-auto space-y-10">
      
      {/* Profile Header & Role Toggle */}
      <div className="flex flex-col md:flex-row items-center justify-between gap-6 luxury-glass p-8 rounded-2xl">
        <div className="flex items-center gap-6">
          <div className="w-20 h-20 rounded-full bg-gradient-to-br from-[#D4AF37] to-[#8B0000] p-1">
            <div className="w-full h-full rounded-full bg-[#0A0A0A] flex items-center justify-center">
              <User className="w-8 h-8 text-[#D4AF37]" />
            </div>
          </div>
          <div>
            <h1 className="text-3xl font-serif text-white font-bold">
              Namaste, <span className="text-[#D4AF37]">Rohan</span>
            </h1>
            <p className="text-gray-400 mt-1">Verified {role === "buyer" ? "Collector" : "Artisan"}</p>
          </div>
        </div>

        {/* Role Toggle */}
        <div className="flex items-center bg-black/40 p-1.5 rounded-full border border-white/10">
          <button 
            onClick={() => setRole("buyer")}
            className={`px-6 py-2.5 rounded-full text-sm font-bold uppercase tracking-widest transition-all ${
              role === "buyer" ? "bg-white text-black" : "text-gray-400 hover:text-white"
            }`}
          >
            Buyer
          </button>
          <button 
            onClick={() => setRole("vendor")}
            className={`px-6 py-2.5 rounded-full text-sm font-bold uppercase tracking-widest transition-all ${
              role === "vendor" ? "bg-[#8B0000] text-white" : "text-gray-400 hover:text-white"
            }`}
          >
            Vendor
          </button>
        </div>
      </div>

      {/* DASHBOARDS */}
      <motion.div 
        key={role}
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        {role === "buyer" ? <BuyerDashboard /> : <VendorDashboard />}
      </motion.div>

    </div>
  );
};

// ==============================
// BUYER VIEW
// ==============================
const BuyerDashboard = () => (
  <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
    <div className="md:col-span-1 space-y-6">
      <div className="luxury-glass p-6 rounded-2xl">
        <h3 className="text-xl font-serif text-white mb-4">Quick Links</h3>
        <ul className="space-y-2">
          <li className="flex items-center gap-3 text-[#D4AF37] bg-[#D4AF37]/10 p-3 rounded-lg cursor-pointer font-medium">
            <Package className="w-5 h-5" /> My Orders
          </li>
          <li className="flex items-center gap-3 text-gray-400 hover:text-white hover:bg-white/5 p-3 rounded-lg cursor-pointer transition-colors font-medium">
            <Heart className="w-5 h-5" /> Wishlist
          </li>
          <li className="flex items-center gap-3 text-red-400 hover:text-red-300 hover:bg-red-500/10 p-3 rounded-lg cursor-pointer transition-colors mt-6 font-medium">
            <LogOut className="w-5 h-5" /> Sign Out
          </li>
        </ul>
      </div>
    </div>

    <div className="md:col-span-2 space-y-6">
      <div className="luxury-glass p-8 rounded-2xl border-[#D4AF37]/20">
        <h2 className="text-2xl font-serif text-white mb-6">Recent Orders</h2>
        <div className="space-y-4">
          {[1, 2].map((i) => (
            <div key={i} className="flex flex-col sm:flex-row items-center gap-6 p-4 rounded-xl bg-black/40 border border-white/5">
              <div className="w-20 h-20 rounded-lg bg-gray-800 overflow-hidden shrink-0">
                <img src="https://images.unsplash.com/photo-1605814515286-3ec4ceac05ee?q=80&w=200&auto=format&fit=crop" alt="Item" className="w-full h-full object-cover opacity-80" />
              </div>
              <div className="flex-1 text-center sm:text-left">
                <h4 className="text-lg font-bold text-white">Banarasi Silk Saree</h4>
                <p className="text-sm text-gray-400">Order #IND-409{i}2</p>
                <span className="inline-block mt-2 px-2 py-1 bg-green-500/20 text-green-400 text-xs font-bold rounded">Delivered</span>
              </div>
              <div className="text-right">
                <p className="text-xl font-serif font-bold text-[#D4AF37]">₹4,500</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  </div>
);

// ==============================
// VENDOR VIEW
// ==============================
const VendorDashboard = () => (
  <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
    <div className="md:col-span-1 space-y-6">
      <div className="luxury-glass p-6 rounded-2xl border-[#8B0000]/30 glow-border">
        <h3 className="text-xl font-serif text-white mb-4">Shop Menu</h3>
        <ul className="space-y-2">
          <li className="flex items-center gap-3 text-white bg-[#8B0000]/60 p-3 rounded-lg cursor-pointer font-medium">
            <Upload className="w-5 h-5" /> Upload Product
          </li>
          <li className="flex items-center gap-3 text-gray-400 hover:text-white hover:bg-white/5 p-3 rounded-lg cursor-pointer transition-colors font-medium">
            <Store className="w-5 h-5" /> My Stalls
          </li>
          <li className="flex items-center gap-3 text-gray-400 hover:text-white hover:bg-white/5 p-3 rounded-lg cursor-pointer transition-colors font-medium">
            <Tag className="w-5 h-5" /> Sales History
          </li>
        </ul>
      </div>
      
      <div className="luxury-glass p-6 rounded-2xl">
        <p className="text-gray-400 text-sm">Total Revenue</p>
        <h2 className="text-4xl font-serif text-[#D4AF37] mt-1">₹45,200</h2>
        <p className="text-green-400 text-sm mt-2">+12% this month</p>
      </div>
    </div>

    <div className="md:col-span-2 space-y-6">
      <div className="luxury-glass p-8 rounded-2xl border-white/10">
        <h2 className="text-2xl font-serif text-white mb-2">Upload Heritage Item</h2>
        <p className="text-gray-400 text-sm mb-8">List a new product to your regional market. Connect buyers with tradition.</p>
        
        <form className="space-y-5" onSubmit={(e) => { e.preventDefault(); alert("Mock API: Product Uploaded Successfully!"); }}>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
            <div className="space-y-2">
              <label className="text-sm font-bold text-gray-300">Product Name</label>
              <input type="text" placeholder="e.g. Pashmina Shawl" className="w-full bg-black/40 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-[#D4AF37] transition-colors" required />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-bold text-gray-300">Price (₹)</label>
              <input type="number" placeholder="5000" className="w-full bg-black/40 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-[#D4AF37] transition-colors" required />
            </div>
          </div>
          
          <div className="space-y-2">
            <label className="text-sm font-bold text-gray-300">Select Market / Stall</label>
            <select className="w-full bg-black/40 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-[#D4AF37] transition-colors">
              <option>Johari Bazaar (Jaipur)</option>
              <option>Chandni Chowk (Delhi)</option>
            </select>
          </div>

          <div className="space-y-2">
            <label className="text-sm font-bold text-gray-300">Upload Image</label>
            <div className="w-full border-2 border-dashed border-white/20 rounded-xl p-8 flex flex-col items-center justify-center text-gray-400 hover:text-white hover:border-[#D4AF37] transition-colors cursor-pointer bg-black/20">
              <Image className="w-8 h-8 mb-2" />
              <span className="text-sm">Click to browse or drag & drop</span>
            </div>
          </div>

          <button type="submit" className="w-full py-4 rounded-xl bg-gradient-to-r from-[#D4AF37] to-[#8B0000] text-white font-bold uppercase tracking-widest hover:opacity-90 transition-opacity glow-border mt-4">
            Publish Item
          </button>
        </form>
      </div>
    </div>
  </div>
);
