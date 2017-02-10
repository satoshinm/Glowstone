package net.glowstone.util.noise;

import com.aparapi.Kernel;

import java.util.Random;

/*
 * A speed-improved simplex noise algorithm
 *
 * Based on example code by Stefan Gustavson (stegu@itn.liu.se).
 * Optimisations by Peter Eastman (peastman@drizzle.stanford.edu).
 * Better rank ordering method by Stefan Gustavson in 2012.
 *
 * This could be sped up even further, but it's useful as is.
 *
 * Version 2012-03-09
 */
public class SimplexNoise extends PerlinNoise {
    protected static final double SQRT_3 = 1.7320508075688772; // Math.sqrt(3)
    protected static final double F2 = 0.5 * (SQRT_3 - 1);
    protected static final double G2 = (3 - SQRT_3) / 6;
    protected static final double G22 = G2 * 2.0 - 1;
    protected static final double F3 = 1.0 / 3.0;
    protected static final double G3 = 1.0 / 6.0;
    protected static final double G32 = G3 * 2.0;
    protected static final double G33 = G3 * 3.0 - 1.0;
    private static final Grad[] grad3 = {new Grad(1, 1, 0), new Grad(-1, 1, 0), new Grad(1, -1, 0), new Grad(-1, -1, 0),
            new Grad(1, 0, 1), new Grad(-1, 0, 1), new Grad(1, 0, -1), new Grad(-1, 0, -1),
            new Grad(0, 1, 1), new Grad(0, -1, 1), new Grad(0, 1, -1), new Grad(0, -1, -1)};
    protected final int[] permMod12 = new int[512];

    public SimplexNoise(Random rand) {
        super(rand);
        for (int i = 0; i < 512; i++) {
            permMod12[i] = perm[i] % 12;
        }
    }

    public static int floor(double x) {
        return x > 0 ? (int) x : (int) x - 1;
    }

    protected static double dot(Grad g, double x, double y) {
        return g.x * x + g.y * y;
    }

    protected static double dot(Grad g, double x, double y, double z) {
        return g.x * x + g.y * y + g.z * z;
    }

    @Override
    protected double[] get2dNoise(double[] noise, double x, double z, int sizeX, int sizeY, double scaleX, double scaleY, double amplitude) {
        int index = 0;
        for (int i = 0; i < sizeY; i++) {
            double zin = offsetY + (z + i) * scaleY;
            for (int j = 0; j < sizeX; j++) {
                double xin = offsetX + (x + j) * scaleX;
                noise[index++] += simplex2D(xin, zin) * amplitude;
            }
        }
        return noise;
    }

    @Override
    protected double[] get3dNoise(double[] noise, double x, double y, double z, int sizeX, int sizeY, int sizeZ, double scaleX, double scaleY, double scaleZ, double amplitude) {
        int index = 0;
        for (int i = 0; i < sizeZ; i++) {
            double zin = offsetZ + (z + i) * scaleZ;
            for (int j = 0; j < sizeX; j++) {
                double xin = offsetX + (x + j) * scaleX;
                for (int k = 0; k < sizeY; k++) {
                    double yin = offsetY + (y + k) * scaleY;
                    noise[index++] += simplex3D(xin, yin, zin) * amplitude;
                }
            }
        }
        return noise;
    }

    @Override
    public double noise(double xin, double yin) {
        xin += offsetX;
        yin += offsetY;
        return simplex2D(xin, yin);
    }

    @Override
    public double noise(double xin, double yin, double zin) {
        xin += offsetX;
        yin += offsetY;
        zin += offsetZ;
        return simplex3D(xin, yin, zin);
    }

    private double simplex2D(double xin, double yin) {
        double n0, n1, n2; // Noise contributions from the three corners

        // Skew the input space to determine which simplex cell we're in
        double s = (xin + yin) * F2; // Hairy factor for 2D
        int i = floor(xin + s);
        int j = floor(yin + s);
        double t = (i + j) * G2;
        double dX0 = i - t; // Unskew the cell origin back to (x,y) space
        double dY0 = j - t;
        double x0 = xin - dX0; // The x,y distances from the cell origin
        double y0 = yin - dY0;

        // For the 2D case, the simplex shape is an equilateral triangle.

        // Determine which simplex we are in.
        int i1, j1; // Offsets for second (middle) corner of simplex in (i,j) coords
        if (x0 > y0) {
            i1 = 1; // lower triangle, XY order: (0,0)->(1,0)->(1,1)
            j1 = 0;
        } else {
            i1 = 0; // upper triangle, YX order: (0,0)->(0,1)->(1,1)
            j1 = 1;
        }

        // A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
        // a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
        // c = (3-sqrt(3))/6

        double x1 = x0 - i1 + G2; // Offsets for middle corner in (x,y) unskewed coords
        double y1 = y0 - j1 + G2;
        double x2 = x0 + G22; // Offsets for last corner in (x,y) unskewed coords
        double y2 = y0 + G22;

        // Work out the hashed gradient indices of the three simplex corners
        int ii = i & 255;
        int jj = j & 255;
        int gi0 = permMod12[ii + perm[jj]];
        int gi1 = permMod12[ii + i1 + perm[jj + j1]];
        int gi2 = permMod12[ii + 1 + perm[jj + 1]];

        // Calculate the contribution from the three corners
        double t0 = 0.5 - x0 * x0 - y0 * y0;
        if (t0 < 0) {
            n0 = 0.0;
        } else {
            t0 *= t0;
            n0 = t0 * t0 * dot(grad3[gi0], x0, y0); // (x,y) of kernelGrad3 used for 2D gradient
        }

        double t1 = 0.5 - x1 * x1 - y1 * y1;
        if (t1 < 0) {
            n1 = 0.0;
        } else {
            t1 *= t1;
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
        }

        double t2 = 0.5 - x2 * x2 - y2 * y2;
        if (t2 < 0) {
            n2 = 0.0;
        } else {
            t2 *= t2;
            n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
        }

        // Add contributions from each corner to get the final noise value.
        // The result is scaled to return values in the interval [-1,1].
        return 70.0 * (n0 + n1 + n2);
    }

    private int[] grad3_1 = new int[]{1, 1, 0};
    private int[] grad3_2 = new int[]{-1, 1, 0};
    private int[] grad3_3 = new int[]{1, -1, 0};
    private int[] grad3_4 = new int[]{-1, -1, 0};
    private int[] grad3_5 = new int[]{1, 0, 1};
    private int[] grad3_6 = new int[]{-1, 0, 1};
    private int[] grad3_7 = new int[]{1, 0, -1};
    private int[] grad3_8 = new int[]{-1, 0, -1};
    private int[] grad3_9 = new int[]{0, 1, 1};
    private int[] grad3_10 = new int[]{0, -1, 1};
    private int[] grad3_11 = new int[]{0, 1, -1};
    private int[] grad3_12 = new int[]{0, -1, -1};
    private double[] noise = new double[1];

    class SimplexKernel extends Kernel {
        private static final double F3 = 1.0 / 3.0;
        private static final double G3 = 1.0 / 6.0;
        private static final double G32 = G3 * 2.0;
        private static final double G33 = G3 * 3.0 - 1.0;
        private final double xin;
        private final double yin;
        private final double zin;

        SimplexKernel(final double xin, final double yin, final double zin) {
            this.xin = xin;
            this.yin = yin;
            this.zin = zin;
        }

        @Override
        public void run() {
            // Add contributions from each corner to get the final noise value.
            // The result is scaled to stay just inside [-1,1]
            double n0, n1, n2, n3; // Noise contributions from the four corners

            // Skew the input space to determine which simplex cell we're in
            double s = (xin + yin + zin) * F3; // Very nice and simple skew factor for 3D
            int i = (int) floor(xin + s);
            int j = (int) floor(yin + s);
            int k = (int) floor(zin + s);
            double t = (i + j + k) * G3;
            double dX0 = i - t; // Unskew the cell origin back to (x,y,z) space
            double dY0 = j - t;
            double dZ0 = k - t;
            double x0 = xin - dX0; // The x,y,z distances from the cell origin
            double y0 = yin - dY0;
            double z0 = zin - dZ0;

            // For the 3D case, the simplex shape is a slightly irregular tetrahedron.

            // Determine which simplex we are in.
            int i1, j1, k1; // Offsets for second corner of simplex in (i,j,k) coords
            int i2, j2, k2; // Offsets for third corner of simplex in (i,j,k) coords
            if (x0 >= y0) {
                if (y0 >= z0) {
                    i1 = 1; // X Y Z order
                    j1 = 0;
                    k1 = 0;
                    i2 = 1;
                    j2 = 1;
                    k2 = 0;
                } else if (x0 >= z0) {
                    i1 = 1; // X Z Y order
                    j1 = 0;
                    k1 = 0;
                    i2 = 1;
                    j2 = 0;
                    k2 = 1;
                } else {
                    i1 = 0; // Z X Y order
                    j1 = 0;
                    k1 = 1;
                    i2 = 1;
                    j2 = 0;
                    k2 = 1;
                }
            } else { // x0<y0
                if (y0 < z0) {
                    i1 = 0; // Z Y X order
                    j1 = 0;
                    k1 = 1;
                    i2 = 0;
                    j2 = 1;
                    k2 = 1;
                } else if (x0 < z0) {
                    i1 = 0; // Y Z X order
                    j1 = 1;
                    k1 = 0;
                    i2 = 0;
                    j2 = 1;
                    k2 = 1;
                } else {
                    i1 = 0; // Y X Z order
                    j1 = 1;
                    k1 = 0;
                    i2 = 1;
                    j2 = 1;
                    k2 = 0;
                }
            }

            // A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
            // a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z), and
            // a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z), where
            // c = 1/6.
            double x1 = x0 - i1 + G3; // Offsets for second corner in (x,y,z) coords
            double y1 = y0 - j1 + G3;
            double z1 = z0 - k1 + G3;
            double x2 = x0 - i2 + G32; // Offsets for third corner in (x,y,z) coords
            double y2 = y0 - j2 + G32;
            double z2 = z0 - k2 + G32;
            double x3 = x0 + G33; // Offsets for last corner in (x,y,z) coords
            double y3 = y0 + G33;
            double z3 = z0 + G33;

            // Work out the hashed gradient indices of the four simplex corners
            int ii = i & 255;
            int jj = j & 255;
            int kk = k & 255;
            int gi0 = permMod12[ii + perm[jj + perm[kk]]];
            int gi1 = permMod12[ii + i1 + perm[jj + j1 + perm[kk + k1]]];
            int gi2 = permMod12[ii + i2 + perm[jj + j2 + perm[kk + k2]]];
            int gi3 = permMod12[ii + 1 + perm[jj + 1 + perm[kk + 1]]];

            // Calculate the contribution from the four corners
            double t0 = 0.5 - x0 * x0 - y0 * y0 - z0 * z0;
            if (t0 < 0) {
                n0 = 0.0;
            } else {
                t0 *= t0;
                n0 = t0 * t0 * dot(getGrad3(gi0), x0, y0, z0);
            }

            double t1 = 0.5 - x1 * x1 - y1 * y1 - z1 * z1;
            if (t1 < 0) {
                n1 = 0.0;
            } else {
                t1 *= t1;
                n1 = t1 * t1 * dot(getGrad3(gi1), x1, y1, z1);
            }

            double t2 = 0.5 - x2 * x2 - y2 * y2 - z2 * z2;
            if (t2 < 0) {
                n2 = 0.0;
            } else {
                t2 *= t2;
                n2 = t2 * t2 * dot(getGrad3(gi2), x2, y2, z2);
            }

            double t3 = 0.5 - x3 * x3 - y3 * y3 - z3 * z3;
            if (t3 < 0) {
                n3 = 0.0;
            } else {
                t3 *= t3;
                n3 = t3 * t3 * dot(getGrad3(gi3), x3, y3, z3);
            }

            noise[0] = 32.0 * (n0 + n1 + n2 + n3);
        }

        private double dot(int[] g, final double x, final double y, final double z) {
            return g[0] * x + g[1] * y + g[2] * z;
        }

        public double getNoise() {
            return noise[0];
        }

        public int[] getGrad3(int index) {
            if (index == 0) {
                return grad3_1;
            }
            if (index == 1) {
                return grad3_2;
            }
            if (index == 2) {
                return grad3_3;
            }
            if (index == 3) {
                return grad3_4;
            }
            if (index == 4) {
                return grad3_5;
            }
            if (index == 5) {
                return grad3_6;
            }
            if (index == 6) {
                return grad3_7;
            }
            if (index == 7) {
                return grad3_8;
            }
            if (index == 8) {
                return grad3_9;
            }
            if (index == 9) {
                return grad3_10;
            }
            if (index == 10) {
                return grad3_11;
            }
            if (index == 11) {
                return grad3_12;
            }
            return grad3_1;
        }
    }

    private double simplex3D(double xin, double yin, double zin) {
        SimplexKernel kernel = new SimplexKernel(xin, yin, zin);
        kernel.execute(1);
        return kernel.getNoise();
    }

    // Inner class to speed up gradient computations
    // (array access is a lot slower than member access)
    private static class Grad {
        public final double x, y, z;

        Grad(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
