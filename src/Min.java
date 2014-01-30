class Min extends Numb
{
  protected Numb post;
  
  protected Min()
  {
    post = new O();
    isMin = true;
  }
  protected Min(Numb n)
  {
    post = n;
    isMin = true;
  }
  
  public Numb result()
  {
    return this;
  }
  
  public String toString()
  {
    return "'"+post.toString();
  }
}