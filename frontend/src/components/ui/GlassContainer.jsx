import React from "react";
import { cn } from "../../utils/cn";

export const GlassContainer = ({ children, className, ...props }) => {
  return (
    <div className={cn("glass-panel rounded-3xl", className)} {...props}>
      {children}
    </div>
  );
};
