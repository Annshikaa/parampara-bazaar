import React from "react";
import { Link } from "react-router-dom";
import { Tilt3DCard } from "../ui/Tilt3DCard";
import { Star, ShoppingBag, MessageCircle } from "lucide-react";
import { useCart } from "../../store/CartContext";

export const ProductCard = ({ product }) => {
  const { addToCart } = useCart();
  
  return (
    <Tilt3DCard className="luxury-glass overflow-hidden group flex flex-col h-full border-white/10 hover:border-[#D4AF37]/50 rounded-2xl">
      <Link to={`/product/${product.id}`} className="block relative aspect-[4/5] overflow-hidden rounded-t-2xl bg-black">
        <img 
          src={product.image} 
          alt={product.name}
          className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105 opacity-80 group-hover:opacity-100"
        />
        {/* Soft Gold overlay on hover */}
        <div className="absolute inset-0 bg-gradient-to-t from-black via-black/40 to-transparent opacity-80 group-hover:opacity-60 transition-opacity duration-500" />
        
        {/* Tags */}
        <div className="absolute top-4 left-4">
          <span className="px-3 py-1.5 rounded-full text-[10px] font-sans font-bold tracking-widest bg-[#D4AF37]/20 text-[#D4AF37] border border-[#D4AF37]/30 shadow-sm uppercase backdrop-blur-md">
            {product.tag}
          </span>
        </div>
      </Link>
      
      <div className="p-6 flex flex-col gap-4 flex-grow bg-white/5">
        <div className="flex justify-between items-start gap-2">
          <Link to={`/product/${product.id}`} className="block group-hover:text-[#D4AF37] transition-colors">
            <h3 className="font-bold font-serif text-xl line-clamp-2 text-white">{product.name}</h3>
          </Link>
        </div>

        <div className="flex items-center gap-2 mt-auto">
          <div className="flex items-center gap-1 bg-white/10 border border-white/5 px-2 py-1.5 rounded-lg shrink-0">
            <Star className="w-3.5 h-3.5 fill-[#D4AF37] text-[#D4AF37]" />
            <span className="text-xs font-bold text-gray-300">{product.rating}</span>
          </div>
          <p className="text-2xl font-bold text-white ml-auto font-serif tracking-wide">
            ₹{product.price.toLocaleString()}
          </p>
        </div>
        
        <div className="flex items-center gap-3 mt-4">
          <button 
            onClick={() => addToCart({ ...product, price: product.price }, "M")}
            className="flex-1 flex items-center justify-center gap-2 py-3 rounded-lg bg-white/5 hover:bg-white/20 border border-white/10 transition-colors font-medium text-sm text-white"
          >
            <ShoppingBag className="w-4 h-4" />
            Add
          </button>
          
          <Link 
            to={`/bargain/${product.id}`}
            className="flex-1 flex items-center justify-center gap-2 py-3 rounded-lg bg-gradient-to-r from-[#D4AF37] to-[#8B0000] hover:from-[#8B0000] hover:to-[#D4AF37] shadow-[0_0_15px_rgba(212,175,55,0.3)] transition-all font-bold text-sm text-white relative glow-border"
          >
            <MessageCircle className="w-4 h-4" />
            Bargain
          </Link>
        </div>
      </div>
    </Tilt3DCard>
  );
};
