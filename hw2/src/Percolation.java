import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final int N;

    private final boolean[] open;
    private int numOpen;

    private final WeightedQuickUnionUF fullWeightedQuickUnionUF;  // to tell if a site is full
    private final WeightedQuickUnionUF percolatedWeightedQuickUnionUF;  // to tell if the grid percolates
    private final int indexOfTop;
    private final int indexOfBottom;

    public Percolation(int N) {
        if (N < 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;

        open = new boolean[N * N];
        for (int i = 0; i < open.length; i++) {
            open[i] = false;
        }
        numOpen = 0;

        fullWeightedQuickUnionUF = new WeightedQuickUnionUF(N * N + 1);
        percolatedWeightedQuickUnionUF = new WeightedQuickUnionUF(N * N + 2);
        indexOfTop = N * N;
        indexOfBottom = indexOfTop + 1;
    }

    public void open(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IndexOutOfBoundsException();
        }

        // !!! DO NOT re-open a site !!!
        if (isOpen(row, col)) {
            return;
        }

        open[indexOf(row, col)] = true;
        numOpen++;
        unionWithNeighbours(row, col);
    }

    public boolean isOpen(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return open[indexOf(row, col)];
    }

    public boolean isFull(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return fullWeightedQuickUnionUF.connected(indexOf(row, col), indexOfTop);
    }

    public int numberOfOpenSites() {
        return numOpen;
    }

    public boolean percolates() {
        return percolatedWeightedQuickUnionUF.connected(indexOfTop, indexOfBottom);
    }

    private boolean outOfRange(int row, int col) {
        return (row < 0 || row >= N) || (col < 0 || col >= N);
    }

    /**
     * Converts the 2-dimensional row-column coordinates of N-by-N grid
     * to 1-dimensional index of union-find data structure, in row-first order.
     *
     * @param row row number of a site
     * @param col column number of the same site
     * @return the row-first-order integer representing the site in union-find data structure
     */
    private int indexOf(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return row * N + col;
    }

    /**
     * Connects in the Union-Find data structure the site at (row, col) with its 4 neighbours.
     * Called only when the site (row, col) is opened.
     *
     * @param row of the site
     * @param col of the site
     */
    private void unionWithNeighbours(int row, int col) {
        if (outOfRange(row, col)) {
            throw new IndexOutOfBoundsException();
        }

        int indexOfThis = indexOf(row, col);

        // 1. up
        if (row == 0) {
            fullWeightedQuickUnionUF.union(indexOfThis, indexOfTop);
            percolatedWeightedQuickUnionUF.union(indexOfThis, indexOfTop);
        } else if (isOpen(row - 1, col)) {
            fullWeightedQuickUnionUF.union(indexOfThis, indexOf(row - 1, col));
            percolatedWeightedQuickUnionUF.union(indexOfThis, indexOf(row - 1, col));
        }
        // 2. down
        if (row == N - 1) {
            percolatedWeightedQuickUnionUF.union(indexOfThis, indexOfBottom);
        } else if (isOpen(row + 1, col)) {
            fullWeightedQuickUnionUF.union(indexOfThis, indexOf(row + 1, col));
            percolatedWeightedQuickUnionUF.union(indexOfThis, indexOf(row + 1, col));
        }
        // 3. left
        if (col > 0 && isOpen(row, col - 1)) {
            fullWeightedQuickUnionUF.union(indexOfThis, indexOf(row, col - 1));
            percolatedWeightedQuickUnionUF.union(indexOfThis, indexOf(row, col - 1));
        }
        // 4. right
        if (col < N - 1 && isOpen(row, col + 1)) {
            fullWeightedQuickUnionUF.union(indexOfThis, indexOf(row, col + 1));
            percolatedWeightedQuickUnionUF.union(indexOfThis, indexOf(row, col + 1));
        }
    }

}
