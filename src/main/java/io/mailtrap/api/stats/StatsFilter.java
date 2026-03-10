package io.mailtrap.api.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsFilter {
    private String startDate;
    private String endDate;
    private List<Long> sendingDomainIds;
    private List<String> sendingStreams;
    private List<String> categories;
    private List<String> emailServiceProviders;
}
