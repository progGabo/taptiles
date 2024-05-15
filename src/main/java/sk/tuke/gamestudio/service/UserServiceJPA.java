package sk.tuke.gamestudio.service;

import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.Users;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
@Service
public class UserServiceJPA implements UserService{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Users getUser(String login) {
        try {
            return (Users) entityManager.createNamedQuery("User.findUser")
                    .setParameter("login", login)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public boolean addUser(Users user) {
        if(getUser(user.getLogin()) == null) {
            entityManager.persist(user);
            return true;
        }
        return false;
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("User.resetUsers").executeUpdate();
    }
}
