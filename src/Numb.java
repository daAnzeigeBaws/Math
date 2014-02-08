import java.io.*;

abstract class Numb implements FormulaPart
{
  private enum Setting {DECIMAL, BINARY, OCTAL};
  
  private static Setting setting = Setting.DECIMAL;
  protected boolean isInt = false;
  protected boolean isO = false;
  protected boolean isMin = false;
  
  public static final Numb ZERO = new O();
  public static final Numb ONE = new Nat();
  public static final Numb TEN = new Nat(new Nat(new Nat(new Nat(new Nat(new Nat(new Nat(new Nat(new Nat(ONE)))))))));
  
  public static void main(String[] s) throws Exception
  {
    doStuff();
  }
  private static void doStuff() throws Exception
  {
    System.out.println("\n\n\nthis is a simple calculator\n'exit' closes this program\n'help' prints a list of the commands\n\n");
    
    while(true)
    {
      System.out.print("\nsmc>");
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String str = br.readLine();
      
      if(str.compareToIgnoreCase("exit") == 0)
      {
        System.exit(0);
      }
      else if(str.compareToIgnoreCase("help") == 0)
      {
        printSyntax();
      }
      else if(str.toUpperCase().contains("/HELP"))
      {
        printSyntax(str);
      }
      else
      {
        System.out.println(analyseSyntax(str));
      }
    }
  }
  private static void printSyntax()
  {
    System.out.println("\n\nif you want more specific information just type command/help\n\n");
    System.out.println("SET\t\t\tsets the current representation of the numbers");
    System.out.println("BINARY\t\t\ttransforms the given number into a binary number");
    System.out.println("OCTAL\t\t\ttransforms the given number into a octal number");
    System.out.println("+\t\t\tadds two numbers");
    System.out.println("-\t\t\tsubtracts the second number from the first");
    System.out.println("*\t\t\tmultiplys both numbers");
    System.out.println("/\t\t\tdevides the first number by the second");
  }
  private static void printSyntax(String s)
  {
    String str = s.split("/")[0];
    str = str.toLowerCase();
    switch(str)
    {
      case "set":
      {
        System.out.println("\nsets the current representation of the numbers");
        System.out.println("syntax:\t\t\t\tset <argument>");
        System.out.println("possible arguments:\t\tdecimal,binary");
        break;
      }
      case "+":
      {
        System.out.println("\nadds two numbers");
        System.out.println("syntax:\t\t\t\t<argument>+<argument>");
        System.out.println("possible arguments:\t\tevery valid number, depending\n\t\t\t\ton the current representation");
        break;
      }
      case "-":
      {
        System.out.println("\nsubtracts the second number from the first");
        System.out.println("syntax:\t\t\t\t<argument>-<argument>");
        System.out.println("possible arguments:\t\tevery valid number, depending\n\t\t\t\ton the current representation");
        break;
      }
    }
  }
  private static String analyseSyntax(String s) throws Exception
  {
    if(s.toLowerCase().startsWith("binary"))
    {
      s = s.substring(6);
      s = s.trim();
      try{s = binaryNotation(toNumb(s));}
      catch(Exception e){}
    }
    else if(s.toLowerCase().startsWith("octal"))
    {
      s = s.substring(5);
      s = s.trim();
      try{s = octalNotation(toNumb(s));}
      catch(Exception e){}
    }
    else if(s.toLowerCase().startsWith("set"))
    {
      s = s.substring(3);
      s = s.trim();
      if(s.compareToIgnoreCase("decimal") == 0)
      {
        setting = Setting.DECIMAL;
        return "new setting: decimal";
      }
      else if(s.compareToIgnoreCase("binary") == 0)
      {
        setting = Setting.BINARY;
        return "new setting: binary";
      }
      else if(s.compareToIgnoreCase("octal") == 0)
      {
        setting = Setting.OCTAL;
        return "new setting: octal";
      }
    }
    else
    {
      if(setting == Setting.DECIMAL)
      return decimalNotation(calculate(s.toLowerCase()));
    }
    return s;
  }
  private static Numb calculate(String s)
  {
    s = s.trim();
    try
    {
      if(s.contains("+"))
      {
        Numb n = ZERO;
        for(int i = 0; i < s.split("\\+").length; i++)
        {
          n = add(calculate(s.split("\\+")[i]),n);
        }
        return n;
      }
      else if(s.contains("-"))
      {
        Numb n = calculate(s.split("\\-")[0]);
        for(int i = 1; i < s.split("\\-").length; i++)
        {
          n = sub(n,calculate(s.split("\\-")[i]));
        }
        return n;
      }
      else if(s.contains("*"))
      {
        Numb n = ONE;
        for(int i = 0; i < s.split("\\*").length; i++)
        {
          n = mult(calculate(s.split("\\*")[i]),n);
        }
        return n;
      }
      else if(s.contains("/"))
      {
        Numb n = toNumb(s.split("\\/")[0]);
        for(int i = 1; i < s.split("\\/").length; i++)
        {
          n = div(n,calculate(s.split("\\/")[i]));
        }
        return n;
      }
      else if(s.contains("%"))
      {
        Numb n = toNumb(s.split("\\%")[0]);
        for(int i = 1; i < s.split("\\%").length; i++)
        {
          n = modulo(n,calculate(s.split("\\%")[i]));
        }
        return n;
      }
      else
      {
        return toNumb(s);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return ZERO;
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
  
  public static Numb modulo(Numb n1, Numb n2) throws FatalMathException
  {
    if(n1.isMin)
    {
      return ZERO;
    }
    if(n2.isMin)
    {
      return ZERO;
    }
    if(n2.isO)
    {
      throw new FatalMathException();
    }
    if(n1.isO)
    {
      return ZERO;
    }
    if(areEqual(n1,n2))
    {
      return ZERO;
    }
    
    return sub(n1,mult(div(n1,n2),n2));
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
  }
  
  public static String binaryNotation(Numb n) throws Exception
  {
    if(n.isO)
    {
      return "0";
    }
    else if(n.isMin)
    {
      return "";
    }
    return binaryNotationHelp(n,"");
  }
  private static String binaryNotationHelp(Numb n, String s) throws Exception
  {
    if(n.isO)
    {
      return s;
    }
    if(areEqual(modulo(n,new Nat(ONE)),ZERO))
    {
      return binaryNotationHelp(div(n,new Nat(ONE)),"0"+s);
    }
    return binaryNotationHelp(div(n,new Nat(ONE)),"1"+s);
  }
  
  public static String octalNotation(Numb n) throws Exception
  {
    if(n.isO)
    {
      return "0";
    }
    Numb eight = sub(TEN,new Nat(ONE));
    
    return decimalNotation(div(n,eight)) +
    decimalNotation(modulo(n,eight));
  }
  
  public static Numb toNumb(String s)
  {
    Numb n = new O();
    if(setting == Setting.DECIMAL)
    {
      for(int i = 0; i < s.length(); i++)
      {
        Numb tmp = intToNumb(Integer.parseInt(""
        +s.charAt(-1*(i-(s.length()-1)))));
        try{tmp = mult(tmp,pov(TEN,intToNumb(i)));}
        catch(Exception e){System.out.println(e);}
        n = add(tmp,n);
      }
    }
    else if(setting == Setting.BINARY)
    {
      for(int i = 0; i < s.length(); i++)
      {
        Numb tmp = intToNumb(Integer.parseInt(""
        +s.charAt(-1*(i-(s.length()-1)))));
        try{n = add(n,mult(tmp,pov(new Nat(ONE),intToNumb(i))));}
        catch(Exception e){System.out.println(e);}
      }
    }
    return n;
  }
  
  public static Numb intToNumb(int i)
  {
    Numb n = new O();
    if(i > 0)
    {
      for(int in = 0; in < i; in++)
      {
        n = new Nat(n);
      }
    }
    if(i < 0)
    {
      for(int in = 0; in < i; in++)
      {
        n = new Min(n);
      }
    }
    return n;
  }
}