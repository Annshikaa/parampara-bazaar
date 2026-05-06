import React from "react";
import Tilt from "react-parallax-tilt";
import { cn } from "../../utils/cn";

export const Tilt3DCard = ({ children, className, ...props }) => {
  return (
    <Tilt
      tiltMaxAngleX={10}
      tiltMaxAngleY={10}
      perspective={1000}
      scale={1.02}
      transitionSpeed={1000}
      gyroscope={true}
      glareEnable={true}
      glareMaxOpacity={0.25}
      glarePosition="all"
      glareBorderRadius="1rem"
      className={cn("rounded-2xl transition-all duration-300 shadow-soft hover:shadow-lift border border-white/5", className)}
      {...props}
    >
      {children}
    </Tilt>
  );
};
