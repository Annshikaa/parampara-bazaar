import { useNavigate } from "react-router-dom";
import PageShell from "../components/layout/PageShell";
import Button from "../components/ui/Button";

export default function NotFound() {
  const navigate = useNavigate();
  return (
    <PageShell>
      <div className="glass rounded-4xl p-10 shadow-soft text-center">
        <h1 className="text-3xl font-extrabold">404</h1>
        <p className="mt-2 text-sm text-black/65">This page dipped.</p>
        <div className="mt-6 flex justify-center gap-2">
          <Button onClick={() => navigate("/")}>Go Home</Button>
          <Button variant="ghost" onClick={() => navigate("/explore")}>Explore</Button>
        </div>
      </div>
    </PageShell>
  );
}