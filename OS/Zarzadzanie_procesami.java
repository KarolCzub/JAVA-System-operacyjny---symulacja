package OS;

import java.util.Iterator;
import java.util.LinkedList;

public class Zarzadzanie_procesami {
	private procesor pr1;
	public Zarzadzanie_procesami(procesor pr)
	{
		pr1=pr;
	}
	public static LinkedList<PCB> lista_PCB = new LinkedList<PCB>();
	public int nowy_proces(String naz, int prio)
	{
		PCB proces = new PCB(naz,prio);
		lista_PCB.add(proces);
		System.out.println("Stworzono proces "+proces.id);
		pr1.DodajProces(proces);
		return proces.id;		
	}
	public void usun_proces(int identyfikator)
	{
		Iterator<PCB> it = lista_PCB.iterator();
		PCB pom;
		PCB usun = null;
		pr1.UsunProces(identyfikator);
		while (it.hasNext()) {
			pom = it.next();
			if(pom.id==identyfikator)
			{
				usun=pom;
			}
			
		}
		if(usun!=null)
		{
			System.out.println("Usunieto proces "+usun.id);	
			lista_PCB.remove(usun);
		}
		else
		{
			System.out.println("Brak procesu");
		}
	}
	public PCB znajdz_proces(int identyfikator)
	{
		Iterator<PCB> it = lista_PCB.iterator();
		PCB pom;
		while (it.hasNext()) 
		{
			pom = it.next();
			if(pom.id==identyfikator)
			{
				return pom;
			}
		}
		return null;
	}
	public static PCB zwroc_proces(int identyfikator)
	{
		Iterator<PCB> it = lista_PCB.iterator();
		PCB pom;
		while (it.hasNext()) 
		{
			pom = it.next();
			if(pom.id==identyfikator)
			{
				return pom;
			}	
		}
		return null;
	}
	/*
	public void zapisz_stan(PCB proces)
	{
		//proces.rejestr_A=A;
		//proces.rejestr_B=B;
		//proces.licznik=licznik;
	}
	public void wczytaj_stan(PCB proces)
	{
		//A=proces.rejestr_A;
		//B=proces.rejestr_B;
		//proces.licznik=licznik;
	}
	*/
	public void lista_procesow()
	{
		Iterator<PCB> it = lista_PCB.iterator();
		PCB pom;
		System.out.println("Lista procesow:");
		int licz = 0;
		while (it.hasNext()) 
		{
			pom = it.next();
			System.out.println("Proces identyfikator: "+pom.id);
			licz++;
		}
		if(licz==0)
		{
			System.out.println("Pusta");
		}
	}
	public void wypisz_proces(int id)
	{
		PCB pom = zwroc_proces(id);
		if(pom==null)
		{
			System.out.println("Brak procesu o podanym identyfikatorze");
		}
		else
		{
			System.out.print("Proces "+pom.id+" priorytet: "+pom.priorytet);
			if (pom.stan==1)
			{
				System.out.println(" Stan: Gotowy");
			}
			else if(pom.stan==2)
			{
				System.out.println(" Stan: Wykonywany");
			}
			else
			{
				System.out.println(" Stan: Wstrzymany");
			}
			System.out.println("Stan rejestrów:");
			System.out.println("Rejestr A: "+pom.rejestr_A+" rejestr B: "+pom.rejestr_B+" rejestr C: "+pom.rejestr_C);
			System.out.println("Licznik rozkazów: "+pom.licznik);
		}
	}
	public void zmien_priorytet(int identyfikator, int prio)
	{
		PCB pom = zwroc_proces(identyfikator);
		pom.priorytet=pom.priorytet+prio;
	}
	
}
