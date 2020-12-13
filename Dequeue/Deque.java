import edu.princeton.cs.algs4.StdIn;
import java.util.NoSuchElementException;
import java.util.Iterator;


public class Deque<Item> implements Iterable<Item> {

	private class Node {
		Item value;
		Node next;
		Node prev;
	}

	private Node front;
	private Node back;
	private int size;

	//construct an empty deque
	public Deque() {
		this.front = null;
		this.back = null;
		this.size = 0;
	}

	//is the deque empty?
	public boolean isEmpty() {
		return size == 0;
	}

	// return the number of itesm on the deque
	public int size() {
		return this.size;
	}

	// add the item to the front
	public void addFirst(Item item) {
		if (item == null) throw new IllegalArgumentException();
		Node newElement = new Node();
		newElement.value = item;
		if (this.size == 0) {
			this.front = newElement;
			this.back = newElement;
		} else {
			newElement.prev = this.front;
			this.front.next = newElement;
			this.front = newElement;
		}
		this.size++;
	}

	// add the item to the back
	public void addLast(Item item) {
		if (item == null) throw new IllegalArgumentException();
		Node newElement = new Node();
		newElement.value = item;
		if (this.size == 0) {
			this.front = newElement;
			this.back = newElement;
		} else {
			newElement.next = this.back;
			this.back.prev= newElement;
			this.back = newElement;
		}
		this.size++;
	}

	// remove and return the item from the front
	public Item removeFirst() {
		if (this.size == 0) throw new NoSuchElementException();
		Node returnable = this.front;
		if (this.size > 1) {
			this.front.prev.next = null;
			this.front = this.front.prev;
		} else {
			this.front = null;
			this.back = null;
		}
		this.size--;
		return returnable.value;
	}

	// remove and return the item from the back
	public Item removeLast() {
		if (this.size == 0) throw new NoSuchElementException();
		Node returnable = this.back;
		if (this.size > 1) {
			this.back.next.prev = null;
			this.back = this.back.next;
		} else {
			this.front = null;
			this.back = null;
		}
		this.size--;
		return returnable.value;
	}

	// return an iterator over items in order from front to back
	public Iterator<Item> iterator() {
		return new DequeIterator();
	}

	private class DequeIterator implements Iterator<Item> {
		private Node current = front;

		public boolean hasNext() {
			return this.current != null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();
			Item item = current.value;
			current = current.prev;
			return item;
		}
	}

	public static void main(String[] args) {
	}
}
