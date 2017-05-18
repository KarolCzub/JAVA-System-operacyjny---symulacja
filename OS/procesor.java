package OS;


import java.util.LinkedList;

public class procesor
{
	LinkedList <PCB> [] TabKolProc=new LinkedList[16];
	PCB running=null;
	boolean TablicaNiepustych[] = new boolean[16];
	
	public int ZwrocNajwyzszaNiepusta()
	{
		int max = 0;
		for (int i=0; i<=15; i++)
		{
			if(TablicaNiepustych[i]==true) max=i;
		}
		return max;
	}
	
	public boolean CzyJedyny(int a)
	{
		if (TabKolProc[a].size()==1) return true;
		else return false;
	}
	
	public void DodajLiczniki()
	{
		int a=ZwrocNajwyzszaNiepusta();
		for (int i=a; i>0; i--)
		{
			if (TablicaNiepustych[i]==true)
			{
				for (int j=0; j<TabKolProc[i].size(); j++)
				{
					PCB pom = TabKolProc[i].get(j);
					if (pom.stan==1) pom.counterBezczynnosci++;
					if (pom.stan==2) pom.counter++;
				}
			}
		}
	}

	public void UstawPriorytety()
	{
		int a=ZwrocNajwyzszaNiepusta();
		for (int i=a; i>0; i--)
		{
			if (TablicaNiepustych[i]==true)
			{
				for (int j=0; j<TabKolProc[i].size(); j++)
				{
					PCB pom = TabKolProc[i].get(j);
					if ((pom.stan==1) && pom.counterBezczynnosci>8 && pom.priorytet!=15) 
					{
						pom.priorytet=15;
						System.out.println ("zwiekszono priorytet procesu o id: " + pom.id + " ze wzgledu na okres bezczynnosci");
						TabKolProc[i].remove(j);
						TabKolProc[15].add(pom);
						TablicaNiepustych[15]=true;
						if (TabKolProc[i].size()==0) TablicaNiepustych[i]=false;
					}
					else if ((pom.stan==2) && pom.counter>3 && pom.priorytet>pom.priorytet_bazowy)
					{
						TabKolProc[i].remove(j);
						System.out.println ("powrot do bazowego priorytetu procesu o id: " + pom.id);
						if (TabKolProc[pom.priorytet_bazowy].size()==0) TablicaNiepustych[pom.priorytet_bazowy]=true;
						TabKolProc[pom.priorytet_bazowy].add(pom);
						pom.priorytet=pom.priorytet_bazowy;
						if (TabKolProc[i].size()==0) TablicaNiepustych[i]=false;
					}
				}
			}
		}
	}
	
	public void UstawNastepny() throws InterruptedException
	{		
		int a=ZwrocNajwyzszaNiepusta();
		boolean znalazl=false;
		if (running==null && a>0)
		{
			ustawianie:
			for (int i=a; i>0; i--)
			{
				if (TablicaNiepustych[i]==true)
				{
					for (int j=0; j<TabKolProc[i].size(); j++)
					{
						PCB pom = TabKolProc[i].get(j);
						if (pom.stan==1) 
						{
							running=pom;
							running.counterBezczynnosci=0;
							running.counter=0;
							TabKolProc[i].remove(j);
							TabKolProc[i].add(pom);
							pom.Proces_Wykonywany();
							//zarzadca procesami wczytuje liczniki obecnego running i zmienia stan na 2
							System.out.println ("Zaladowano do running proces o id: " + pom.id);
							znalazl=true;
							break ustawianie;
						}
					}
				}
			}
		}
		else if (a==0) Thread.sleep(100); //////////////////////////////////////////////////TO DO PRZEMYSLENIA
		else if ((a==running.priorytet) && (CzyJedyny(a)==true) && (running.stan==2 || running.stan==1) && (running.counter>3)) 
		{
			running.counter=0;
			System.out.println ("Proces obecny w running dostaje kolejny kwant czasu, ze wzgledu na najwyzszy priorytet");
		} 
		else
		{	
			if (a>running.priorytet && running.counter<4)
			{
				int pom2=running.priorytet;
			ustawianie2:
				for (int i=a; i>pom2; i--)
				{
					if (TablicaNiepustych[i]==true)
					{
						for (int j=0; j<TabKolProc[i].size(); j++)
						{
							PCB pom = TabKolProc[i].get(j);
							if (pom.stan==1)
							{
								// zarzadcaProcesami zapisuje stan obecnego running i zmienia stan
								if (running.stan!=3) running.Proces_Gotowy();
								running=pom;
								System.out.println ("Wywlaszczono procesor dla procesu o id: " + pom.id + " ze wzgledu na wyzszy priorytet");
								running.counterBezczynnosci=0;
								running.counter=0;
								TabKolProc[i].remove(j);
								TabKolProc[i].addFirst(pom);
								running.Proces_Wykonywany();
								// zarzadcaProcesami wczytuje liczniki obecnego running i zmienia stan na 2
								znalazl=true;
								break ustawianie2;
							}
						}
					}
				}
			}
			else if (running.stan==3) 
			{
				int pom2=running.priorytet;
				if (TabKolProc[pom2].element()==running) 
				{
					TabKolProc[pom2].add(TabKolProc[pom2].poll());
				}
				ustawianie2:
				for (int i=a; i>0; i--)
				{
					if (TablicaNiepustych[i]==true)
					{
						for (int j=0; j<TabKolProc[i].size(); j++)
						{
							PCB pom = TabKolProc[i].get(j);
							if (pom.stan==1)
							{
								// zarzadcaProcesami zapisuje stan obecnego running i zmienia stan
								running=pom;
								System.out.println ("Zaladowano do running proces o id: " + pom.id);
								running.counterBezczynnosci=0;
								running.counter=0;
								TabKolProc[i].remove(j);
								TabKolProc[i].add(pom);
								running.Proces_Wykonywany();
								// zarzadcaProcesami wczytuje liczniki obecnego running i zmienia stan na 2
								znalazl=true;
								break ustawianie2;
							}
							//else if (pom.stan==2)
							//{
							//	running.counter=0;
							//	System.out.println ("Proces obecny w running dostaje kolejny kwant czasu, ze wzgledu na najwyzszy priorytet");
							//	znalazl = true;
							//}
						}
					}
				}
			} 	
			else if (running.counter>3) 
			{
				int pom2=running.priorytet;
				if (TabKolProc[pom2].element()==running) 
				{
					TabKolProc[pom2].add(TabKolProc[pom2].poll());
				}
				ustawianie2:
				for (int i=a; i>=pom2; i--)
				{
					if (TablicaNiepustych[i]==true)
					{
						for (int j=0; j<TabKolProc[i].size(); j++)
						{
							PCB pom = TabKolProc[i].get(j);
							if (pom.stan==1)
							{
								// zarzadcaProcesami zapisuje stan obecnego running i zmienia stan
								if (running.stan!=3) running.Proces_Gotowy();
								running=pom;
								System.out.println ("Zaladowano do running proces o id: " + pom.id);
								running.counterBezczynnosci=0;
								running.counter=0;
								TabKolProc[i].remove(j);
								TabKolProc[i].add(pom);
								running.Proces_Wykonywany();
								// zarzadcaProcesami wczytuje liczniki obecnego running i zmienia stan na 2
								znalazl=true;
								break ustawianie2;
							}
							//else if (pom.stan==2)
							//{
							//	running.counter=0;
							//	System.out.println ("Proces obecny w running dostaje kolejny kwant czasu, ze wzgledu na najwyzszy priorytet");
							//	znalazl = true;
							//}
						}
					}
				}
			} 
		}
		if (znalazl==false) Thread.sleep(100);
	}
	
	public void Dzialaj(Interpreter1 slawek) throws InterruptedException
	{
		UstawPriorytety();
		UstawNastepny();
		if (running!=null && running.stan!=3) 
		{
			slawek.WykonajRozkaz(running);
			DodajLiczniki();
		}
		else{
			System.out.println("Brak aktywnego procesu");
			Thread.sleep(100); // do przemyslenia
		}
	}
	/*
	public void PrzepiszTablice(PCB zarzadzanie)
	{
		for (int i=0; i<zarzadzanie.lista_PCB.size(); i++)
		{
			TabKolProc[zarzadzanie.lista_PCB.get(i).priorytet].add(zarzadzanie.lista_PCB.get(i));
			TablicaNiepustych[zarzadzanie.lista_PCB.get(i).priorytet]=true;
		}
	}*/
	
	public void DodajProces(PCB proces)
	{
		TabKolProc[proces.priorytet].add(proces);
		TablicaNiepustych[proces.priorytet]=true;
		System.out.println("Dodano proces o id: " + proces.id + " do kolejki o priorytecie " + proces.priorytet);
	}
	
	public void WypiszGotowe()
	{
		int a=ZwrocNajwyzszaNiepusta();
		if (a<1) System.out.println("Brak procesow do wyswietlenia");
		else
		{	
		for (int i=a; i>0; i--)
		{
			if (TablicaNiepustych[i]==true)
			{
				System.out.print("Tab[" + i + "] = ");
				for (int j=0; j<TabKolProc[i].size(); j++)
				{
					PCB pom = TabKolProc[i].get(j);
					System.out.print("proces o id: " + pom.id + " i stanie: " + pom.stan + " ");
				}
			}
			System.out.println();
		}
		}
	}

	public void UsunProces(int iden)
	{
		if (running.id==iden) running=null;
		int a=ZwrocNajwyzszaNiepusta();
		szukanie:
		for (int i=a; i>0; i--)
		{
			if (TablicaNiepustych[i]==true)
			{
				for (int j=0; j<TabKolProc[i].size(); j++)
				{
					PCB pom = TabKolProc[i].get(j);
					if (pom.id==iden)
					{
						TabKolProc[i].remove(j);
						if (TabKolProc[i].size()==0) TablicaNiepustych[i]=false;
						break szukanie;
					}
				}
			}
		}
	}
		
	public procesor()
	{
		for (int i=0; i<=15; i++)
		{
			TabKolProc[i]=new LinkedList <PCB>();
			TablicaNiepustych[i]=false;
		}
	}
	
}

