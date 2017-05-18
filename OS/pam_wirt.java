package OS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

	public class pam_wirt {
	static int ilosc_ramek = 128;
	 static int wielkosc_strony = 16;
	 char[] pamiec = new char[ilosc_ramek*wielkosc_strony]; // 2048
	FAT8 system_plikow;
	public List<Integer> wolne_ramki = new ArrayList<Integer>();
	public List<Integer> zajete_ramki = new ArrayList<Integer>();
	
	
	pam_wirt(FAT8 system)
	{
		for(int i=0;i<ilosc_ramek;i++)
		{
			wolne_ramki.add(i);
		}
		system_plikow =system;
	}
	
	public void wpisz_do_pamieci_wirtualnej(int procesID, String Sname)
	{
		byte[] file = system_plikow.open_file(Sname);
		
		int rozmiar = file[0] * 256 + file[1]+2;
			
		PageTable pt = new PageTable(rozmiar);
		
		PCB p = Zarzadzanie_procesami.zwroc_proces(procesID);
		p.tablica_stronnic.jak_duza(rozmiar);
		
		
		int ile_ramek = ile_ramek_potrzeba(rozmiar);		
		
		if(wolne_ramki.size()>=ile_ramek)
		{
			//Iterator<Integer> myListIterator = wolne_ramki.iterator();
			//int fr = wolne_ramki.get(0);	// numer pierwszej wolnej strony	
			int indeks_tablicy_str=0;
			int nr=0;
			for(int pozycja_w_pliku=2, przesuniecie_w_pamieci=0;pozycja_w_pliku<rozmiar;pozycja_w_pliku++,przesuniecie_w_pamieci++)
			{
				
				if(przesuniecie_w_pamieci%16==0 )
				{
					 nr = wolne_ramki.remove(0);
					zajete_ramki.add(nr);
					p.tablica_stronnic.str[indeks_tablicy_str].nr_w_pam_wt = nr;
					//pt.str[indeks_tablicy_str].nr_w_pam_wt = nr;
					indeks_tablicy_str++;
				}
				
				
				
				pamiec[nr*wielkosc_strony +przesuniecie_w_pamieci%16] = (char) file[pozycja_w_pliku];
				//this.wypisz_pamiec();
			}
		
		}
	}

	public void usun_proces_z_pam_wirt(int procesID)
	{
		
	}
	
	public char [] wyslij_ramke(int nr_ramki)
	{
		char[] buf = new char [16];
		for(int i=0;i<16;i++)
		{
			buf[i] = pamiec[nr_ramki*wielkosc_strony + i];
		}
		return  buf;
	}
	
	public  void wypisz_pamiec()
	  {
		  for(int i=0;i<ilosc_ramek*wielkosc_strony;i++)
		  {
			  System.out.print(pamiec[i] + "");
		  }
	  }
	
	public int ile_ramek_potrzeba(int rozmiar)
	{
		int ile_ramek;
		if(rozmiar%wielkosc_strony!=0) 
		{
			ile_ramek = rozmiar / wielkosc_strony + 1;
		}
		else ile_ramek = rozmiar / wielkosc_strony;
		return ile_ramek;
	}
	
	public void wyswietl_liste_wolnych_ramek()
	{
		Iterator<Integer> myListIterator = wolne_ramki.iterator();
		while(myListIterator.hasNext())
		{
			int val = myListIterator.next();
			System.out.print(val + " ");
		}
	}

	public void wyswietl_liste_zajetych_ramek()
	{
		Iterator<Integer> myListIterator = zajete_ramki.iterator();
		while(myListIterator.hasNext())
		{
			int val = myListIterator.next();
			System.out.print(val + " ");
		}
	}

}
