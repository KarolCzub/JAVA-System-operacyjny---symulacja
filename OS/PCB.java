package OS;
public class PCB {
	static int ost_id=1;
	int id; //PID - numer procesu
	int stan; //stan procesu
	// gotowy - 1
	//uruchomiony - 2
	// oczekujacy, wstrzymany - 3
	int rejestr_A = 0;
	int rejestr_B = 0;
	int rejestr_C = 0;
	int licznik;
	int counter = 0;
	int counterBezczynnosci = 0;
	int couter_rozkaz=0;
	String nazwa_pliku;
	public PageTable tablica_stronnic = new PageTable();
	// zarzadzanie pamiecia
	int priorytet_bazowy;
	int priorytet;
	MsgQueue msgqueue = new MsgQueue();
	public semaphore semafor = new semaphore("Message_semaphore",0);//semafor do komunikacji
	PCB(String naz, int prio)
	{
		nazwa_pliku = naz;
		id = ost_id;
		ost_id++;
		 rejestr_A = 0;
		 rejestr_B = 0;
		 rejestr_C = 0;
		 licznik=	0;
		 counter = 0;
		 counterBezczynnosci = 0;
		 couter_rozkaz=0;
		if(prio<1)
		{
			System.out.println("Priorytet za niski, proces dostaje priorytet 1.");
			priorytet_bazowy=1;
			priorytet=1;
		}
		else if (prio>15)
		{
			System.out.println("Priorytet za wysoki, proces dostaje priorytet 15.");
			priorytet_bazowy=15;
			priorytet=15;
		}
		else
		{
			priorytet_bazowy=prio;
			priorytet=prio;
		}
		stan=1;
	}
	public void Proces_Wstrzymany()
	{
		stan=3;
		System.out.println(id+" Zmieniono stan na wstrzymany");
	}
	public void Proces_Gotowy()
	{
		stan=1;
		System.out.println(id+" Zmieniono stan na gotowy");
		//zapisz stan rejestrow
	}
	public void Proces_Wykonywany()
	{
		stan=2;
		System.out.println(id+" Zmieniono stan na wykonywany");
		//wczytanie stanu rejestrow
	}
}
