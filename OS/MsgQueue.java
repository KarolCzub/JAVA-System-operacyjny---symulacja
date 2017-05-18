package OS;


import java.util.LinkedList;

public class MsgQueue {
	private LinkedList<Message> queue = new LinkedList<Message>();
	
	public void insert(int PID, String msg) {
		queue.addLast(new Message(PID, msg));
	}
	
	public Message remove() {
		return queue.pollFirst();
	}
	
	public String print() {
		String out = new String();
		for (int i = 0; i < queue.size(); i++) {
			Message msg = queue.get(i);
			out += msg.PID + ": " + msg.msg +"\n";
		}
		return out;
	}
};