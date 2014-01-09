package org.tonguetied.keywordmanagement;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.Predicate;

/**
 * This predicate is used to find {@link Translation}s based off its 
 * primary key.
 * 
 * @author bsion
 *
 */
public class TranslationIdPredicate implements Predicate
{
    private Set<Long> ids;
    
    /**
     * Create a new instance of TranslationPredicate.
     * 
     * @param id the <code>id</code> of the translation to find
     */
    public TranslationIdPredicate(final Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id cannot be null");
        }
        this.ids = new HashSet<Long>();
        this.ids.add(id);
    }
    
    /**
     * Create a new instance of TranslationPredicate.
     * 
     * @param ids the <code>id</code> of the translation to find
     */
    public TranslationIdPredicate(final Set<Long> ids)
    {
        if (ids == null)
        {
            throw new IllegalArgumentException("id cannot be null");
        }
        this.ids = ids;
    }
    
    /** 
     * Evaluate if a {@link Translation}s business keys are equal. This  
     * method evaluates if the {@link Language}, {@link Bundle} and
     * {@link Country} are equal
     * 
     * @return <code>true</code> if the {@link Translation} business keys
     * match. <code>false</code> otherwise
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    public boolean evaluate(Object object)
    {
        final Translation translation = (Translation) object;
        boolean result;
        if (translation == null)
        {
            result = false;
        }
        else
        {
            result = ids.contains(translation.getId());
        }
        
        return result;
    }
}