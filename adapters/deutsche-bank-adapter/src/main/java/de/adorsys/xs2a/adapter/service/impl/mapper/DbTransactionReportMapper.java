/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.xs2a.adapter.service.impl.mapper;

import de.adorsys.xs2a.adapter.service.model.TransactionsReport;
import de.adorsys.xs2a.adapter.service.impl.model.DbLinks;
import de.adorsys.xs2a.adapter.service.impl.model.DbTransactionReport;
import de.adorsys.xs2a.adapter.service.model.Link;
import org.mapstruct.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface DbTransactionReportMapper {

    default TransactionsReport toTransactionReport(DbTransactionReport report) {
        TransactionsReport transactionsReport = new TransactionsReport();
        Map<String, Link> linkMap = new HashMap<>();
        DbLinks links = report.getLinks();
        Link account = links.getAccount();
        Link balance = links.getBalance();
        List<Link> downloads = links.getDownloads();
        linkMap.put("account", account);
        linkMap.put("balance", balance);
        for (int i = 0; i < downloads.size(); i++) {
            Link download = downloads.get(i);
            linkMap.put("download"+i, download);
        }
        transactionsReport.setLinks(linkMap);
        return transactionsReport;
    }
}
