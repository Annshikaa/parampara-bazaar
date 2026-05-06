import React, { useRef, useMemo } from 'react';
import { Canvas, useFrame, useThree } from '@react-three/fiber';
import { Points, PointMaterial, Float, Sparkles, Box, Cylinder } from '@react-three/drei';
import * as THREE from 'three';

// Manually move camera based on window scroll to achieve parallax natively
const CameraParallax = () => {
  useFrame((state) => {
    const scrollY = window.scrollY || document.documentElement.scrollTop;
    // Move the camera down as the user scrolls down, making the background move up at varied speeds
    state.camera.position.y = THREE.MathUtils.lerp(state.camera.position.y, -scrollY * 0.002, 0.1);
  });
  return null;
};

// Architecture Arches Mix (Basic geometry to simulate Indian layered arches)
const ArchitecturalPillars = () => {
  return (
    <group position={[0, -2, -10]}>
      {/* Front Layer - Darker Silhouette */}
      <group position={[0, 0, 5]}>
        {[-8, -4, 0, 4, 8].map((x, i) => (
          <mesh key={`p_f_${i}`} position={[x, 0, 0]}>
            <cylinderGeometry args={[0.5, 0.5, 15, 16]} />
            <meshStandardMaterial color="#3a0202" roughness={0.9} />
          </mesh>
        ))}
      </group>
      
      {/* Mid Layer - Warmer Terracotta */}
      <group position={[0, 2, -5]}>
        {[-10, -5, 0, 5, 10].map((x, i) => (
          <mesh key={`p_m_${i}`} position={[x, 0, 0]}>
            <cylinderGeometry args={[0.8, 0.8, 20, 16]} />
            <meshStandardMaterial color="#800000" roughness={0.8} />
          </mesh>
        ))}
      </group>

      {/* Deep Layer - Glowing Base */}
      <group position={[0, 5, -15]}>
        {[-15, -7.5, 0, 7.5, 15].map((x, i) => (
          <mesh key={`p_b_${i}`} position={[x, 0, 0]}>
            <boxGeometry args={[3, 25, 3]} />
            <meshStandardMaterial color="#f59e0b" roughness={0.5} />
          </mesh>
        ))}
      </group>
    </group>
  );
};

// Warm Glowing Marigold/Diya Particles
const WarmBazaarParticles = ({ count = 500 }) => {
  const points = useRef();

  const [positions, colors] = useMemo(() => {
    const pos = new Float32Array(count * 3);
    const col = new Float32Array(count * 3);
    const colorChoices = [
      new THREE.Color('#f59e0b'), // Saffron
      new THREE.Color('#daa520'), // Gold
      new THREE.Color('#cc4e00'), // Terracotta
    ];

    for (let i = 0; i < count; i++) {
      pos[i * 3] = (Math.random() - 0.5) * 40; // x
      pos[i * 3 + 1] = (Math.random() - 0.5) * 40; // y
      pos[i * 3 + 2] = (Math.random() - 0.5) * 20 - 5; // z

      const color = colorChoices[Math.floor(Math.random() * colorChoices.length)];
      col[i * 3] = color.r;
      col[i * 3 + 1] = color.g;
      col[i * 3 + 2] = color.b;
    }
    return [pos, col];
  }, [count]);

  useFrame((state, delta) => {
    if (points.current) {
      points.current.rotation.y += delta * 0.02;
    }
  });

  return (
    <Points ref={points} positions={positions} colors={colors}>
      <PointMaterial
        transparent
        vertexColors
        size={0.15}
        sizeAttenuation={true}
        depthWrite={false}
        blending={THREE.AdditiveBlending}
        opacity={0.8}
      />
    </Points>
  );
};

export function CulturalBackground3D() {
  return (
    <div className="fixed inset-0 -z-50 bg-[#fdf5e6] overflow-hidden pointer-events-none">
      <Canvas camera={{ position: [0, 0, 10], fov: 60 }} gl={{ antialias: true }}>
        <fog attach="fog" args={['#fdf5e6', 10, 40]} />
        <ambientLight intensity={1.5} color="#ffd700" />
        <spotLight 
          position={[0, 10, 5]} 
          angle={0.5} 
          penumbra={1} 
          intensity={2} 
          color="#f59e0b" 
        />
        <pointLight position={[0, -5, -5]} intensity={1} color="#cc4e00" />
        
        <ArchitecturalPillars />
        <WarmBazaarParticles count={800} />
        
        <Sparkles count={200} scale={20} size={4} speed={0.2} color="#daa520" opacity={0.6} />
        <CameraParallax />
      </Canvas>
    </div>
  );
}
