class Int extends Numb
{
  protected Numb pre;
  protected Numb post;
  
  protected Int(Numb pr, Numb po)
  {
    pre = pr;
    post = po;
    isInt = true;
  }
  
  public Numb result()
  {
    return this;
  }
}