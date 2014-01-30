abstract class Numb implements FormulaPart
{
  protected boolean isInt = false;
  protected boolean isO = false;
  protected boolean isMin = false;
  
  public static final Numb ZERO = new O();
  public static final Numb ONE = new Nat();
  public static final Numb TEN = new Nat(new Nat(new Nat(new Nat(new Nat(new Nat(new Nat(new Nat(new Nat(ONE)))))))));
  
  public static void main(String[] s) throws Exception
  {
    System.out.println(Numb.decimalNotation(pov(new Nat(ONE),TEN)));
  }
  
  public static Numb add(Numb n1, Numb n2)
  {
    if(n1.isO)
    {
      return n2;
    }
    else if(n2.isO)
    {
      return n1;
    }
    else
    {
      Nat n = (Nat) n2;
      return add(new Nat(n1),n.pre);
    }
  }
  
  public static Numb sub(Numb n1, Numb n2)
  {
    if(n1.isO)
    {
      return negate(n2);
    }
    else if(n2.isO)
    {
      return n1;
    }
    else if(n1.isMin)
    {
      if(n2.isMin)
      {
        return sub(negate(n2),n1);
      }
      else
      {
        return negate(add(negate(n1),negate(n2)));
      }
    }
    else if(n2.isMin)
    {
      if(n1.isMin)
      {
        return sub(negate(n1),n2);
      }
      else
      {
        return add(n1,n2);
      }
    }
    else
    {
      Nat na1 = (Nat) n1;
      Nat na2 = (Nat) n2;
      return sub(na1.pre,na2.pre);
    }
  }
  
  public static Numb negate(Numb n)
  {
    if(n.isO)
    {
      return n;
    }
    else
    {
      return negateHelp(n,ZERO);
    }
  }
  private static Numb negateHelp(Numb n1, Numb n2)
  {
    if(n1.isO)
    {
      return n2;
    }
    else if(!n1.isMin)
    {
      Nat n = (Nat) n1;
      return negateHelp(n.pre,new Min(n2));
    }
    else
    {
      Min m = (Min) n1;
      return negateHelp(m.post,new Nat(n2));
    }
  }
  
  public static Numb mult(Numb n1, Numb n2)
  {
    if(n1.isO)
    {
      return ZERO;
    }
    else if(n2.isO)
    {
      return ZERO;
    }
    else if(n1.isMin)
    {
      if(n2.isMin)
      {
        return mult(negate(n1),negate(n2));
      }
      else
      {
        return negate(mult(negate(n1),n2));
      }
    }
    else if(n2.isMin)
    {
      if(n1.isMin)
      {
        return mult(negate(n1),negate(n2));
      }
      else
      {
        return negate(mult(negate(n2),n1));
      }
    }
    else
    {
      return multHelp(n1,n2,ZERO);
    }
  }
  private static Numb multHelp(Numb n1, Numb n2, Numb n3)
  {
    if(n2.isO)
    {
      return n3;
    }
    else
    {
      Nat na = (Nat) n2;
      return multHelp(n1,na.pre,add(n1,n3));
    }
  }
  
  public static Numb div(Numb n1, Numb n2) throws FatalMathException
  {
    if(n1.isMin)
    {
      if(n2.isMin)
      {
        return divHelp(negate(n1),negate(n2),ZERO);
      }
      else
      {
        return negate(divHelp(negate(n1),n2,ZERO));
      }
    }
    else
    {
      if(n2.isMin)
      {
        return negate(divHelp(n1,negate(n2),ZERO));
      }
      else
      {
        return divHelp(n1,n2,ZERO);
      }
    }
  }
  private static Numb divHelp(Numb n1, Numb n2, Numb r) throws FatalMathException
  {
    if(n2.isO)
    {
      throw new FatalMathException();
    }
    else
    {
      if(n1.isO)
      {
        return r;
      }
      else if(isBiggerThan(n2,n1))
      {
        return r;
      }
      else
      {
        return divHelp(subDiv(n1,n2),n2,new Nat(r));
      }
    }
  }
  private static Numb subDiv(Numb n1, Numb n2)
  {
    if(n2.isO)
    {
      return n1;
    }
    else if(n1.isO)
    {
      return n1;
    }
    else
    {
      Nat na1 = (Nat) n1;
      Nat na2 = (Nat) n2;
      return subDiv(na1.pre,na2.pre);
    }
  }
  
  public static Numb pov(Numb n1, Numb n2) throws FatalMathException
  {
    if(n2.isMin)
    {
      throw new FatalMathException();
    }
    else
    {
      return povHelp(n1,n2,ONE);
    }
  }
  private static Numb povHelp(Numb n1, Numb n2, Numb n3)
  {
    if(n2.isO)
    {
      return n3;
    }
    else
    {
      Nat na = (Nat) n2;
      return povHelp(n1,na.pre,mult(n1,n3));
    }
  }
  
  public static boolean areEqual(Numb n1, Numb n2)
  {
    if(n1.isO)
    {
      if(n2.isO)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else if(n2.isO)
    {
      if(n1.isO)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else if(n1.isMin)
    {
      if(!n2.isMin)
      {
        return false;
      }
      else
      {
        Min m1 = (Min) n1;
        Min m2 = (Min) n2;
        return areEqual(m1.post,m2.post);
      }
    }
    else if(n2.isMin)
    {
      if(!n1.isMin)
      {
        return false;
      }
      else
      {
        Min m1 = (Min) n1;
        Min m2 = (Min) n2;
        return areEqual(m1.post,m2.post);
      }
    }
    else
    {
      Nat na1 = (Nat) n1;
      Nat na2 = (Nat) n2;
      return areEqual(na1.pre,na2.pre);
    }
  }
  
  public static boolean isBiggerThan(Numb n1, Numb n2)
  {
    if(n1.isO)
    {
      if(n2.isO)
      {
        return false;
      }
      else if(n2.isMin)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else if(n1.isMin)
    {
      if(n2.isMin)
      {
        Nat na1 = (Nat) negate(n1);
        Nat na2 = (Nat) negate(n2);
        return isBiggerThan(na2.pre,na1.pre);
      }
      else
      {
        return false;
      }
    }
    else
    {
      if(n2.isMin)
      {
        return true;
      }
      else if(n2.isO)
      {
        return true;
      }
      else
      {
        Nat na1 = (Nat) n1;
        Nat na2 = (Nat) n2;
        return isBiggerThan(na1.pre,na2.pre);
      }
    }
  }
  
  public static String decimalNotation(Numb n) throws Exception
  {
    if(n.isO)
    {
      return "0";
    }
    else if(n.isMin)
    {
      return "-"+decimalNotationHelp(negate(n),"");
    }
    else
    {
      return decimalNotationHelp(n,"");
    }
  }
  private static String decimalNotationHelp(Numb n, String st) throws Exception
  {
    if(n.isO)
    {
      if(st.compareTo("") == 0)
      {
        return "0";
      }
      else
      {
        return st;
      }
    }
    else
    {
      Numb nu = sub(n,(mult(TEN,div(n,TEN))));
      
      int i = 0;
      while(!nu.isO)
      {
        i++;
        nu = ((Nat)nu).pre;
      }
      
      return decimalNotationHelp(div(n,TEN), i+st);
    }
    
    /*String s = "";
    Numb na = ONE;
    do
    {
    Numb po = pov(TEN,new Nat(na));
    Numb nu = sub(n,(mult(po,div(n,po))));
    
    int i = 0;
    while(!nu.isO)
    {
    i++;
    nu = ((Nat)nu).pre;
    }
    s = i + s;
    na = new Nat(na);
    }
    while(areEqual(n,pov(TEN,na)) || isBiggerThan(n,pov(TEN,na)));*/
  }
}