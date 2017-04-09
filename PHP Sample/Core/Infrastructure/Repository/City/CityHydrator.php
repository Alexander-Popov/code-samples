<?php
declare(strict_types = 1);

namespace app\Core\Infrastructure\Repository\City;

use app\Core\{
    Domain\Model\City\City,
    Infrastructure\Repository\HydratorInterface
};

final class CityHydrator implements HydratorInterface
{
    /**
     * @param array $columns
     * @return City
     */
    public function hydrate(array $columns) : City
    {
        return City::create(
            $columns['id'],
            $columns['name'],
            $columns['country'],
            floatval($columns['extra_charge'])
        );
    }

    /**
     * @param $entity
     * @return array
     */
    public function extract($entity) : array 
    {
        /* @var $entity City */
        $columns = [
            'name' => $entity->getName(),
            'country' => $entity->getCountry(),
            'extra_charge' => $entity->getExtraCharge()
        ];
        return $columns;
    }
}