import React from "react";
import { Link } from "react-router-dom";
import { Tilt3DCard } from "../components/ui/Tilt3DCard";
import { motion } from "framer-motion";

export const Home = () => {
  return (
    <div className="min-h-screen flex flex-col justify-center pb-20">
      {/* Hero Section */}
      <section className="pt-24 pb-16 flex flex-col items-center text-center px-4 relative z-10">
        <motion.h1 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-5xl md:text-7xl lg:text-8xl font-serif text-white max-w-5xl mx-auto leading-tight tracking-tight mb-8 drop-shadow-2xl"
        >
          Discover India’s<br />
          <span className="text-[#D4AF37] italic">Cultural Markets</span>
        </motion.h1>
        <motion.p 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="text-lg md:text-xl text-gray-400 max-w-2xl mx-auto font-light leading-relaxed"
        >
          Explore heritage, traditions, and timeless craftsmanship through two extraordinary paths.
        </motion.p>
      </section>

      {/* Gateway Section */}
      <section className="max-w-6xl mx-auto px-4 w-full relative z-20">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
          
          {/* Path A: Regions & Items */}
          <motion.div initial={{ opacity: 0, x: -30 }} animate={{ opacity: 1, x: 0 }} transition={{ delay: 0.2 }}>
            <Link to="/famous-items" className="block w-full">
              <Tilt3DCard className="luxury-glass h-[500px] flex flex-col justify-end p-10 relative overflow-hidden group border-white/20 hover:border-[#D4AF37]/50">
                <div className="absolute inset-0 z-0">
                  <img 
                    src="https://images.unsplash.com/photo-1524492412937-b28074a5d7da?q=80&w=800&auto=format&fit=crop" 
                    alt="Regions" 
                    className="w-full h-full object-cover opacity-50 group-hover:scale-110 transition-transform duration-1000 grayscale group-hover:grayscale-0"
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-black via-black/40 to-transparent" />
                </div>
                
                <div className="relative z-10">
                  <div className="h-1 w-12 bg-[#D4AF37] mb-6 rounded-full group-hover:w-24 transition-all duration-500" />
                  <h2 className="text-5xl font-serif text-white mb-4">Explore Regions</h2>
                  <p className="text-gray-300 text-lg">
                    Discover thousands of famous items categorized by historic Indian cities like Jaipur, Varanasi, and more.
                  </p>
                </div>
              </Tilt3DCard>
            </Link>
          </motion.div>

          {/* Path B: Markets */}
          <motion.div initial={{ opacity: 0, x: 30 }} animate={{ opacity: 1, x: 0 }} transition={{ delay: 0.3 }}>
            <Link to="/famous-markets" className="block w-full">
              <Tilt3DCard className="luxury-glass glow-border h-[500px] flex flex-col justify-end p-10 relative overflow-hidden group border-white/20 hover:border-[#8B0000]/50">
                <div className="absolute inset-0 z-0">
                  <img 
                    src="https://images.unsplash.com/photo-1548013146-72479768bada?q=80&w=800&auto=format&fit=crop" 
                    alt="Markets" 
                    className="w-full h-full object-cover opacity-50 group-hover:scale-110 transition-transform duration-1000 grayscale group-hover:grayscale-0"
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-black via-black/40 to-transparent" />
                </div>

                <div className="relative z-10">
                  <div className="h-1 w-12 bg-[#8B0000] mb-6 rounded-full group-hover:w-24 transition-all duration-500" />
                  <h2 className="text-5xl font-serif text-white mb-4">Explore Markets</h2>
                  <p className="text-gray-300 text-lg">
                    Navigate through 220+ iconic bustling bazaars and street markets exactly as they exist.
                  </p>
                </div>
              </Tilt3DCard>
            </Link>
          </motion.div>

        </div>
      </section>
      
    </div>
  );
};
