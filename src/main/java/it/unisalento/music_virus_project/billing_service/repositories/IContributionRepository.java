package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;
import it.unisalento.music_virus_project.billing_service.dto.contribution.TopContributorAggregationDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IContributionRepository extends MongoRepository<Contribution, String> {
    List<Contribution> findAllByFundraisingId(String fundraisingId);
    @Aggregation(pipeline = {
            "{ $match: { fundraisingId: ?0 } }",
            "{ $group: { " +
                    "   _id: '$userId', " +
                    "   totalAmount: { $sum: '$amount' }, " +
                    "   allAnonymous: { $min: { $cond: [ { $eq: ['$visibility', 'ANONYMOUS'] }, 1, 0 ] } } " +
                    "} }",
            "{ $sort: { totalAmount: -1 } }",
            "{ $limit: 3 }",
            "{ $project: { _id: 0, userId: '$_id', totalAmount: 1, allAnonymous: 1 } }"
    })
    List<TopContributorAggregationDTO> findTop3Contributors(String fundraisingId);
}
