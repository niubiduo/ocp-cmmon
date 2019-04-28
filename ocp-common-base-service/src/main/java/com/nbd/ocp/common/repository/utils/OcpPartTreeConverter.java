package com.nbd.ocp.common.repository.utils;

import com.nbd.ocp.common.repository.base.IOcpBaseDo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.data.repository.query.parser.PartTree.OrPart;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author jhb
 */
public final class OcpPartTreeConverter {
    private static Logger logger = LoggerFactory.getLogger(OcpPartTreeConverter.class);

	private OcpPartTreeConverter() {
	}
	public static <T extends IOcpBaseDo>  Predicate toIndexedQuery(Root<T> root, CriteriaBuilder criteriaBuilder, String searchQu, Map<String, Object> parameters, Class clazz) {
		final PartTree                  partTree =new PartTree(searchQu, clazz);
		final Iterator<OrPart> orIt = partTree.iterator();
		List<Predicate> orPredicate=new ArrayList<>();
		while(orIt.hasNext()) {
			final OrPart orPart = orIt.next();
			final Iterator<Part> partIt = orPart.iterator();
			List<Predicate> AndPredicate=new ArrayList<>();
			while(partIt.hasNext()) {
				final Part part = partIt.next();
				String property=part.getProperty().getSegment();
				Part.Type type=part.getType();
                Predicate predicate=convertOperator(root,criteriaBuilder,property,clazz,type,parameters);
                if(predicate==null){
                    continue;
                }
                AndPredicate.add(predicate);
			}
			if(AndPredicate.size()>0){
				orPredicate.add(criteriaBuilder.and(AndPredicate.toArray(new Predicate[AndPredicate.size()])));
			}
		}
		if(orPredicate.size()>0){
			return criteriaBuilder.or(orPredicate.toArray(new Predicate[orPredicate.size()]));
		}else{
			return null;
		}

	}

    private static <T extends IOcpBaseDo>   Predicate convertOperator(Root<T> root, CriteriaBuilder criteriaBuilder, String property, Class propertyClazz, Part.Type type, Map<String, Object> parameters) {


        switch(type) {
            case SIMPLE_PROPERTY: {
                if(property==null||parameters.get(property)==null|| StringUtils.isEmpty(String.valueOf(parameters.get(property)))){
                    return null;
                }
                return criteriaBuilder.equal(root.get(property),parameters.get(property));
            }

            case NEGATING_SIMPLE_PROPERTY: {
                if(property==null||parameters.get(property)==null|| StringUtils.isEmpty(String.valueOf(parameters.get(property)))){
                    return null;
                }
                return criteriaBuilder.notEqual(root.get(property),parameters.get(property));
            }

            case GREATER_THAN: {
                if(property==null||parameters.get(property)==null|| StringUtils.isEmpty(String.valueOf(parameters.get(property)))){
                    return null;
                }
                return criteriaBuilder.greaterThan(root.get(property),parameters.get(property).toString());
            }

            case GREATER_THAN_EQUAL: {
                if(property==null||parameters.get(property)==null|| StringUtils.isEmpty(String.valueOf(parameters.get(property)))){
                    return null;
                }
                return criteriaBuilder.greaterThanOrEqualTo(root.get(property),parameters.get(property).toString());
            }

            case LESS_THAN: {
                if(property==null||parameters.get(property)==null|| StringUtils.isEmpty(String.valueOf(parameters.get(property)))){
                    return null;
                }
                return criteriaBuilder.lessThan(root.get(property),parameters.get(property).toString());
            }

            case LESS_THAN_EQUAL: {
                if(property==null||parameters.get(property)==null|| StringUtils.isEmpty(String.valueOf(parameters.get(property)))){
                    return null;
                }
                return criteriaBuilder.lessThanOrEqualTo(root.get(property),parameters.get(property).toString());
            }

            case LIKE: {
                if(property==null||parameters.get(property)==null|| StringUtils.isEmpty(String.valueOf(parameters.get(property)))){
                    return null;
                }
                return criteriaBuilder.like(root.get(property),"%"+parameters.get(property)+"%");
            }

            case NOT_LIKE: {
                if(property==null||parameters.get(property)==null|| StringUtils.isEmpty(String.valueOf(parameters.get(property)))){
                    return null;
                }
                return criteriaBuilder.notLike(root.get(property),"%"+parameters.get(property)+"%");
            }

            case BETWEEN: {
                Object start=parameters.get(property+"Start");
                Object end=parameters.get(property+"End");
                if(start==null||end==null){
                    return null;
                }
                return criteriaBuilder.between(root.get(property),start.toString(),end.toString());
            }

            case IS_NOT_NULL: {
                if(property==null){
                    return null;
                }
                return criteriaBuilder.isNotNull(root.get(property));
            }

            case IS_NULL: {
                if(property==null){
                    return null;
                }
                return criteriaBuilder.isNull(root.get(property));
            }

            case IN: {
                try {
                    Object object =  parameters.get(property);
					CriteriaBuilder.In predicateIn=criteriaBuilder.in(root.get(property));
                    if(object instanceof  Collection){
						Collection collection = (Collection) object;
						for(Object item:collection){
							predicateIn.value(item);
						}
					}else if(object instanceof Object[]){
						Object[] objectAry = (Object[]) object;
						for(Object item:objectAry){
							predicateIn.value(item);
						}

					}else{
						predicateIn.value(object);
					}
                    return  predicateIn;
                }catch (Exception e){
                    logger.error("in类型参数转换失败");
                    logger.error(e.getMessage(),e);
                    return null;
                }
            }

            default: {
                throw new MappingException("No matching simpleDB operator for " + type);
            }
        }

    }








	public static  String toIndexedQueryStr(final PartTree tree) {
		final StringBuilder result = new StringBuilder();
		
		final Iterator<OrPart> orIt = tree.iterator();
		while(orIt.hasNext()) {
			
			final OrPart orPart = orIt.next();
			
			final Iterator<Part> partIt = orPart.iterator();
			while(partIt.hasNext()) {
				final Part part = partIt.next();
				
				result.append(" " + part.getProperty().getSegment() + " ");
				result.append(convertOperatorStr(part.getType()));
				
				if(partIt.hasNext()) {
					result.append(" AND ");
				}
			}
			
			if(orIt.hasNext()) {
				result.append(" OR ");
			}
		}
		
		return result.toString();
	}
	
	private static  String convertOperatorStr(final Part.Type type) {
		String result ;
		
		switch(type) {
			case SIMPLE_PROPERTY: {
				result = " = ? ";
				break;
			}
			
			case NEGATING_SIMPLE_PROPERTY: {
				result = " != ? ";
				break;
			}
			
			case GREATER_THAN: {
				result = " > ? ";
				break;
			}
			
			case GREATER_THAN_EQUAL: {
				result = " >= ? ";
				break;
			}
			
			case LESS_THAN: {
				result = " < ? ";
				break;
			}
			
			case LESS_THAN_EQUAL: {
				result = " <= ? ";
				break;
			}
			
			case LIKE: {
				result = " LIKE ? ";
				break;
			}
			
			case NOT_LIKE: {
				result = " NOT LIKE ? ";
				break;
			}
			
			case BETWEEN: {
				result = " BETWEEN ? and ? ";
				break;
			}
			
			case IS_NOT_NULL: {
				result = " IS NOT NULL ";
				break;
			}
			
			case IS_NULL: {
				result = " IS NULL ";
				break;
			}
			
			case IN: {
				result = " IN ? ";
				break;
			}
			
			default: {
				throw new MappingException("No matching simpleDB operator for " + type);
			}
		}
		
		return result;
	}


}




