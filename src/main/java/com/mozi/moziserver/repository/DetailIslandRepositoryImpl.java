//package com.mozi.moziserver.repository;
//
//import com.mozi.moziserver.model.entity.DetailIsland;
//import com.mozi.moziserver.model.entity.QDetailIsland;
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//public class DetailIslandRepositoryImpl extends QuerydslRepositorySupport implements DetailIslandRepositorySupport {
//
//    private final QDetailIsland qDetailIsland=QDetailIsland.detailIsland;
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public DetailIslandRepositoryImpl() {
//        super(DetailIsland.class);
//    }
//}
