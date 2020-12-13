import java.util.Arrays;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private static final int MIN_CAP = 8;
	private Object[] queue;
	private int capacity;
	private int current;

	//construct an empty randomized queue
	public RandomizedQueue() {
		this.queue = new Object[MIN_CAP];
		this.capacity = MIN_CAP;
		this.current = 0;
	}

	// is the randomized queue empty?
	public boolean isEmpty() {
		return this.current == 0;
	}

	// return the number of items on the randomized queue
	public int size() {
		return this.current;
	}

	// add the item
	public void enqueue(Item item) {
		if (item == null) throw new IllegalArgumentException();
		this.current++;
		if (this.current > this.capacity*(0.75)) resizeInc();
		this.queue[this.current] = item;
	}

	private void resizeInc() {
		this.capacity *= 2;
		Object[] newQueue = new Object[this.capacity];
		for (int i = 1; i <= this.current; i++) 
			newQueue[i] = this.queue[i];
		this.queue = newQueue;
	}

	private void resizeDec() {
		this.capacity /= 2;
		Object[] newQueue = new Object[this.capacity];
		for (int i = 1; i <= this.current; i++)
			newQueue[i] = this.queue[i];
		this.queue = newQueue;
	}

	// remove and return a random item
	public Item dequeue() {
		if (isEmpty()) throw new NoSuchElementException();
		randomSwap();
		Item returnable = (Item) this.queue[this.current];
		this.queue[this.current] = null;
		this.current--;
		if (this.current < capacity*0.25 && capacity > MIN_CAP) resizeDec();
		return returnable;
	}

	private void randomSwap() {
		int randomIndex = StdRandom.uniform(1, this.current + 1);
		if (randomIndex == this.current) return;
		Object tmp = this.queue[this.current];
		this.queue[this.current] = this.queue[randomIndex];
		this.queue[randomIndex] = tmp;
	}


	// return a random item (but do not remove it) 
	public Item sample() {
		if (isEmpty()) throw new NoSuchElementException();
		int randomIndex = StdRandom.uniform(1, this.current + 1);
		return (Item) this.queue[randomIndex];
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		return new RandomizedIterator();
	}

	private class RandomizedIterator implements Iterator<Item> { 
		private int[] randomIndexes;	
		private int index;

		public RandomizedIterator() {
			this.index = 1;
			this.randomIndexes = new int[size() + 1];
			for (int i = 0; i < 1000; i++) 
				randomSwap();
			for (int i = 1; i <= size(); i++)
				this.randomIndexes[i] = i;
			StdRandom.shuffle(this.randomIndexes, 1, size());

		}

		public boolean hasNext() {
			return this.index < this.randomIndexes.length;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if  (!hasNext()) throw new NoSuchElementException();
			Item returnable = (Item) queue[this.randomIndexes[this.index]];
			this.index++;
			return returnable;
		}
			
	}

	public static void main(String[] args) {
		RandomizedQueue<String> test = new RandomizedQueue();
		for (int i = 0; i < 10; i++) 
			test.enqueue("" + i);
		for (String s: test) 
			System.out.println(s);
	} 
}
