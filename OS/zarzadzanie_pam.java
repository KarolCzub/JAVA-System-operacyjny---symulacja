package OS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class zarzadzanie_pam {
static int ilosc_ramek = 32;
static int wielkosc_strony = 16;
char[] pamiec = new char[ilosc_ramek*wielkosc_strony]; //512
pam_wirt wirtualna;

public List<Integer> wolne_ramki = new ArrayList<Integer>();
public List<Integer> zajete_ramki = new ArrayList<Integer>();



PageTable pt = new PageTable();
pam_wirt pw ;


public zarzadzanie_pam(FAT8 system)
{
	for(int i=0;i<ilosc_ramek;i++)
	{
		wolne_ramki.add(i);
	}
	wirtualna = new pam_wirt(system);
	
}
/* private int ile_ramek(int rozmiar)
{
	int ile_ramek;
	if(rozmiar%wielkosc_strony!=0) 
	{
		ile_ramek = rozmiar / wielkosc_strony + 1;
	}
	else ile_ramek = rozmiar / wielkosc_strony;
	return ile_ramek;
} */

private void zwolnienie_ramki()		// FIFO
{
	int a;
	a = zajete_ramki.remove(0);
	wolne_ramki.add(a);
}

public char czytaj_z_pam(int virtualAddress, int procesID)
{
	char znak;
	// znalezienie procesu
	PCB p = Zarzadzanie_procesami.zwroc_proces(procesID);
	// sprawdzenie tablicy stronnic
	int nr_ramki = obliczanie_ramki_z_vadd(virtualAddress);
	int strona = 0;
	if(p.tablica_stronnic.str[nr_ramki].nr_w_pam_op==-1) // jak nie ma
	{
		if(wolne_ramki.size()==0)
		{
			zwolnienie_ramki();
		}
		 strona = pobierz_strone(nr_ramki, procesID);
	}
	else strona = p.tablica_stronnic.str[nr_ramki].nr_w_pam_op;
	int przesuniecie = obliczanie_przesuniecia_z_vadd(virtualAddress);
	
	znak = pamiec[strona*wielkosc_strony+przesuniecie];
	return znak;
	
}

public int obliczanie_ramki_z_vadd(int virtualAddress)
{
	return  virtualAddress / wielkosc_strony;
}

public int obliczanie_przesuniecia_z_vadd(int virtualAddress)
{
	int przesuniecie = virtualAddress%wielkosc_strony;
	return przesuniecie;
}

public int pobierz_strone(int ramka, int procesID)
{
	// pcb odpowiedniego procesu
	PCB p = Zarzadzanie_procesami.zwroc_proces(procesID);
	int nr_ramki_do_sciagniecia = p.tablica_stronnic.str[ramka].nr_w_pam_wt;
	char [] buf = new  char [16];
	
	buf = wirtualna.wyslij_ramke(nr_ramki_do_sciagniecia);
	
	Iterator<Integer> myListIterator = wolne_ramki.iterator();
	int fr = myListIterator.next();
	wolne_ramki.remove(0);
	zajete_ramki.add(fr);
	p.tablica_stronnic.str[ramka].nr_w_pam_op = fr;
	
	
	for(int i=0,prz=0;i<wielkosc_strony;i++,prz++)
	{
		pamiec[p.tablica_stronnic.str[ramka].nr_w_pam_op*wielkosc_strony+prz] = buf[i];
	}
	return p.tablica_stronnic.str[ramka].nr_w_pam_op;
}


public void wyczysc_pam()
{
	wolne_ramki.clear();
	zajete_ramki.clear();
	for(int i=0;i<ilosc_ramek;i++)
	{
		wolne_ramki.add(i);
	}
}

public void print_memory()
{
	for(int i=0;i<ilosc_ramek*wielkosc_strony;i++)
	{
		System.out.print(pamiec[i] + "");
	}
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
