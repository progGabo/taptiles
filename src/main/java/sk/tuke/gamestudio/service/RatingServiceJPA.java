package sk.tuke.gamestudio.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;

import java.util.Date;
import java.util.List;


@Transactional
public class RatingServiceJPA implements RatingService{
    @PersistenceContext
    private EntityManager entityManager;


    private Rating findRating(Rating rating){
        try {
            return (Rating) entityManager.createNamedQuery("Rating.getRating")
                    .setParameter("game", rating.getGame())
                    .setParameter("player", rating.getPlayer())
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    public List<Rating> getRatings(String game){
        return entityManager.createNamedQuery("getRatings")
                .setParameter("game", game)
                .getResultList();
    }

    @Override
    public void setRating(Rating rating) throws RatingException {
        Rating object;
        object = findRating(rating);
        if(object == null) {
            entityManager.persist(rating);
        }
        else{
            object.setRating(rating.getRating());
            object.setRatedOn(new Date());
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Object ret = entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game)
                .getSingleResult();
        if(ret == null){
            return 0;
        }
        return (int)(Double.parseDouble(ret.toString()));
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return ((Rating)entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game)
                .setParameter("player", player)
                .getSingleResult()).getRating();
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRating");
    }
}
