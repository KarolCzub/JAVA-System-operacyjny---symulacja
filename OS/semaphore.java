package OS;

import java.util.LinkedList;

public class semaphore {
	
	private int s;
	private LinkedList<PCB> SemaphoreQueqe;
	private String name;
	
	public semaphore(String name, int StartValue)
	{
		this.name=name;
		s=StartValue; // 0 dla komunikatow, 1 dla procesow
		SemaphoreQueqe = new LinkedList<PCB>();
	}
	
	public void waits(PCB Process)
	{
		s=s-1;
		if(s<0)
		{
			System.out.println("Semafor: Doda³em proces do kolejki");
			SemaphoreQueqe.add(Process);
			System.out.println("Semafor: Wstrzymuje proces");
			Process.Proces_Wstrzymany(); // Przekazanie do zmiany stanu na czekanie
		}
	}
	
	public void signal()
	{
		s=s+1;
		if(s<=0)
		{
			System.out.println("Semafor: Zmieniam proces na gotowy");
			PCB ChangeProcess = SemaphoreQueqe.get(0); // Wywolaj dla 1 z listy zmiane stnau na gotowy
			ChangeProcess.Proces_Gotowy();
			System.out.println("Semafor: Usuwam proces z kolejki");
			SemaphoreQueqe.removeFirst();
			// Scheduler 
		}
		else
		{
			s=1;
			System.out.println("Semafor=1");
		}
	}
		
	public void show_semaphore_queue()
	{
		if(SemaphoreQueqe.isEmpty())
		{
			System.out.println("Kolejka semafora jest pusta");
		}
		else
		{
			for(int i=0;i<SemaphoreQueqe.size();i++)
			{
				System.out.println("---"+" ID Procesu:" + SemaphoreQueqe.get(i).id);
			}
		}
	}
}
