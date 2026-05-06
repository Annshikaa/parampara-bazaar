import React from "react";
import { Link, useLocation } from "react-router-dom";
import { ShoppingBag, User } from "lucide-react";
import { useCart } from "../../store/CartContext";

export const Navbar = () => {
  const { cartCount } = useCart();
  const location = useLocation();

  const getLinkClass = (path) => {
    const isActive = location.pathname.startsWith(path) && path !== "/" || location.pathname === path;
    return `text-sm font-medium tracking-wide uppercase transition-colors ${
      isActive ? "text-[#D4AF37]" : "text-gray-400 hover:text-white"
    }`;
  };

  return (
    <nav className="fixed top-0 left-0 right-0 z-50 px-8 py-6">
      <div className="max-w-7xl mx-auto flex items-center justify-between luxury-glass rounded-full px-8 py-4">
        
        <Link to="/" className="flex items-center group">
          <span className="text-2xl font-semibold font-serif text-white tracking-tight group-hover:text-[#D4AF37] transition-colors">
            Parampara<span className="text-[#D4AF37] ml-2 italic">Bazaar</span>
          </span>
        </Link>
        
        <div className="flex items-center gap-10">
          <Link to="/famous-items" className={getLinkClass("/famous-items")}>
            Regions
          </Link>
          <Link to="/famous-markets" className={getLinkClass("/famous-markets")}>
            Markets
          </Link>
          
          <Link 
            to="/cart"
            className="flex items-center gap-2 px-4 py-2 text-gray-300 hover:text-white hover:bg-white/10 rounded-full transition-all border border-white/5 font-medium"
          >
            <ShoppingBag className="w-4 h-4" />
            <span className="text-sm uppercase tracking-wider">Bag</span>
            {cartCount > 0 && (
              <span className="ml-1 bg-[#8B0000] text-white text-[10px] w-4 h-4 flex items-center justify-center rounded-full font-bold">
                {cartCount}
              </span>
            )}
          </Link>

          <Link 
            to="/profile"
            className="flex items-center gap-2 px-4 py-2 text-gray-300 hover:text-white hover:bg-white/10 rounded-full transition-all border border-white/5 font-medium"
          >
            <User className="w-4 h-4" />
            <span className="text-sm uppercase tracking-wider">Profile</span>
          </Link>
        </div>
        
      </div>
    </nav>
  );
};