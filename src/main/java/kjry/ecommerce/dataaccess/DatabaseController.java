package kjry.ecommerce.dataaccess;

public interface DatabaseController<T> {    
    public T findById(String id);
    public boolean create(T entity);
    public boolean update(T entity);
    public boolean removeById(String id);
    public T[] getAll();
}
