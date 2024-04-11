// src/canvas-confetti.d.ts
declare module 'canvas-confetti' {
    interface ConfettiFunction {
      (options?: ConfettiOptions): void;
    }
  
    interface ConfettiOptions {
      particleCount?: number;
      spread?: number;
      startVelocity?: number;
      origin?: { x: number; y: number };
      // Define other options as needed...
    }
  
    const confetti: ConfettiFunction;
    export default confetti;
  }
  