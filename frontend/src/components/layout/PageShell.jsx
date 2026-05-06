import Navbar from "./Navbar";

export default function PageShell({ children }) {
  return (
    <div className="min-h-screen app-bg">
      <Navbar />
      <main className="mx-auto max-w-6xl px-5 pb-16 pt-8">
        <div className="motif">{children}</div>
      </main>
    </div>
  );
}