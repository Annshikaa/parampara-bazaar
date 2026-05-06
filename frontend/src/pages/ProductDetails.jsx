import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { useCart } from "../store/CartContext";
import { Star, MessageCircle, ShoppingBag, ShieldCheck, Truck } from "lucide-react";
import { motion } from "framer-motion";

export const ProductDetails = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [selectedSize, setSelectedSize] = useState("M");
  const { addToCart } = useCart();

  const SIZES = ["S", "M", "L", "XL"];

  useEffect(() => {
    fetch("/mock/products.json")
      .then((res) => res.json())
      .then((data) => {
        const found = data.find((p) => p.id === id);
        setProduct(found);
      });
  }, [id]);

  if (!product) return <div className="py-20 text-center animate-pulse">Unfolding product...</div>;

  return (
    <div className="max-w-6xl mx-auto grid grid-cols-1 md:grid-cols-2 gap-12 py-8">
      
      {/* Image Gallery */}
      <div className="space-y-4">
        <motion.div 
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          className="aspect-[4/5] rounded-3xl overflow-hidden bg-white/5 border border-white/10"
        >
          <img 
            src={product.image} 
            alt={product.name} 
            className="w-full h-full object-cover"
          />
        </motion.div>
      </div>

      {/* Product Info */}
      <div className="flex flex-col gap-6 py-4">
        <div>
          <div className="flex items-center gap-3 mb-3">
            <span className="px-3 py-1 rounded-full text-xs font-semibold bg-white/10 text-white">
              {product.tag}
            </span>
            <div className="flex items-center gap-1 text-sm font-medium text-white/80">
              <Star className="w-4 h-4 text-yellow-500 fill-yellow-500" />
              {product.rating}
            </div>
          </div>
          
          <h1 className="text-4xl font-bold leading-tight mb-2">{product.name}</h1>
          <p className="text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 to-pink-400">
            ₹{product.price.toLocaleString()}
          </p>
        </div>

        <p className="text-lg text-white/70 leading-relaxed font-light">
          {product.description}
        </p>

        {/* Size Selection */}
        <div className="space-y-3">
          <div className="flex justify-between items-center">
            <h3 className="font-medium text-white/90">Select Size</h3>
            <button className="text-sm text-indigo-400 hover:text-indigo-300">Size Guide</button>
          </div>
          <div className="flex gap-3">
            {SIZES.map(size => (
              <button
                key={size}
                onClick={() => setSelectedSize(size)}
                className={`w-14 h-14 rounded-2xl flex items-center justify-center text-lg font-medium transition-all ${
                  selectedSize === size
                    ? "bg-white text-black shadow-[0_0_20px_rgba(255,255,255,0.4)]"
                    : "bg-white/5 border border-white/10 hover:bg-white/10 text-white/70"
                }`}
              >
                {size}
              </button>
            ))}
          </div>
        </div>

        {/* Benefits */}
        <div className="flex flex-col gap-3 py-4 border-y border-white/10">
          <div className="flex items-center gap-3 text-sm text-white/70">
            <ShieldCheck className="w-5 h-5 text-emerald-400" />
            100% Authentic Quality Guaranteed
          </div>
          <div className="flex items-center gap-3 text-sm text-white/70">
            <Truck className="w-5 h-5 text-indigo-400" />
            Fast Delivery across India
          </div>
        </div>

        {/* Actions */}
        <div className="flex gap-4 pt-4">
          <button
            onClick={() => addToCart({ ...product, price: product.price }, selectedSize)}
            className="flex-1 py-4 rounded-2xl bg-white text-black font-semibold text-lg flex items-center justify-center gap-2 hover:bg-white/90 transition-colors"
          >
            <ShoppingBag className="w-5 h-5" />
            Add to Cart
          </button>
          
          <Link
            to={`/bargain/${product.id}`}
            className="flex-1 py-4 rounded-2xl bg-gradient-to-r from-indigo-500 to-purple-600 shadow-glow text-white font-semibold text-lg flex items-center justify-center gap-2 hover:opacity-90 transition-opacity glow-border"
          >
            <MessageCircle className="w-5 h-5" />
            Bargain
          </Link>
        </div>
      </div>
    </div>
  );
};