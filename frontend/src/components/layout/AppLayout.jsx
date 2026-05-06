import React from "react";
import { Outlet } from "react-router-dom";
import { Navbar } from "./Navbar";

export const AppLayout = () => {
  return (
    <div className="min-h-screen relative overflow-x-hidden selection:bg-gold/20 selection:text-maroon">
      <Navbar />
      <main className="pt-24 pb-12 px-6 max-w-7xl mx-auto min-h-screen flex flex-col relative z-10">
        <Outlet />
      </main>
    </div>
  );
};
