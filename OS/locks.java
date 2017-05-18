package OS;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class locks {
	
	private boolean isLock;
	private int id;
	private LinkedList<PCB> LockQueqe;

	locks()
	{
		System.out.println("Stworzono zamek");
		isLock = false;
		LockQueqe = new LinkedList<PCB>();
	}
	
	public void lock(int PID)
	{
		PCB proces = Zarzadzanie_procesami.zwroc_proces(PID);
		if(isLock)
		{
			System.out.println("Zamek: Dodaje Proces do kolejki");
			LockQueqe.add(proces);
			System.out.println("Zamek: Wstrzymuje proces");
			proces.Proces_Wstrzymany(); // Przekazanie do zmiany stanu na czekanie
			// Przekazanie do zmiany stanu na czekanie
		}
		else
		{
			isLock=true;
			id=proces.id;
			System.out.println("Zablokowano zamek");
		}

	}
	
	public void unlock(int PID)
	{
		PCB proces = Zarzadzanie_procesami.zwroc_proces(PID);

		if(LockQueqe.isEmpty() && proces.id==id)
		{
			isLock=false;
			System.out.println("W calosci zwolniono zamek");	
		}

		else if(proces.id==id)
		{
			System.out.println("Zamek: Zmieniam stan na gotowy");
		PCB ChangeProcess=	LockQueqe.get(0) ;// Wywolaj dla 1 z listy zmiane stnau na gotowy
		ChangeProcess.Proces_Gotowy();
		System.out.println("Zamek: Usuwam proces z kolejki");
			LockQueqe.removeFirst();
			id=ChangeProcess.id;

		}
		else if(isLock==false)
		{
			System.out.println("Zamek jest juz odblokowany");
		}
		else if(LockQueqe.isEmpty())
		{
				isLock=false;
				System.out.println("Zamek jest juz odblokowany");
		}
		else 
		{
			System.out.println("ID Procesu nie rowna sie temu id ktore stworzylo zamek");
		}
	}
	
	public void show_lock_queqe()
	{
		if(LockQueqe.isEmpty())
		{
			System.out.println("Kolejka zamka jest pusta");
		}
		
		for(int i=0;i<LockQueqe.size();i++)
		{
			System.out.println("---"+" ID Procesu:"+LockQueqe.get(i).id );
		}
	}
}