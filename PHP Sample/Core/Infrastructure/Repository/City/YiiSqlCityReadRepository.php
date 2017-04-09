<?php
declare(strict_types = 1);

namespace app\Core\Infrastructure\Repository\City;

use app\Core\Domain\Model\City\City;
use app\Core\Domain\Repository\City\CityReadRepository;
use yii\db\Query;

final class YiiSqlCityReadRepository implements CityReadRepository
{
    /**
     * @var CityHydrator
     */
    private $cityHydrator;


    public function __construct()
    {
        $this->cityHydrator = new CityHydrator();
    }

    /**
     * @param $id
     * @return City|null
     */
    public function findById(string $id)
    {
        return $this->findOneByAttribute('id', $id);
    }

    /**
     * @param string $name
     * @param string $country
     * @return City|null
     */
    public function findByNameAndCountry(string $name, string $country)
    {
        $city = $this->findOneByAttributes(['LOWER(name)' => mb_strtolower($name), 'country' => $country]);
        return $city;
    }

    /**
     * @param string $name
     * @param string $country
     * @return bool
     */
    public function existsByNameAndCountry(string $name, string $country) : bool
    {
        $query = new Query();
        return $query->from(YiiSqlCityRepository::TABLE_NAME)
            ->where(['LOWER(name)' => mb_strtolower($name), 'country' => $country])
            ->exists();
    }

    /**
     * @param string $name
     * @param string $country
     * @param string $id
     * @return bool
     */
    public function existsByNameAndCountryAndId(string $name, string $country, string $id) : bool
    {
        $query = new Query();
        return $query->from(YiiSqlCityRepository::TABLE_NAME)
            ->where(['LOWER(name)' => mb_strtolower($name), 'country' => $country, 'id' => $id])
            ->exists();
    }

    /**
     * @param array $attributes
     * @return City|null
     */
    private function findOneByAttributes(array $attributes)
    {
        $query = new Query();
        $row = $query->from(YiiSqlCityRepository::TABLE_NAME)
            ->andWhere($attributes)
            ->one();
        if ($row) {
            return $this->toEntity($row);
        } else {
            return null;
        }
    }

    /**
     * @return City[]
     */
    public function findAll() : array
    {
        $query = new Query();
        $rows = $query->from(YiiSqlCityRepository::TABLE_NAME)->orderBy(['name' => SORT_ASC])->all();
        $cities = [];
        foreach ($rows as $row) {
            array_push($cities, $this->toEntity($row));
        }
        return $cities;
    }

    /**
     * @param $attribute
     * @param $value
     * @return City|null
     */
    private function findOneByAttribute(string $attribute, $value)
    {
        $query = new Query();
        $row = $query->from(YiiSqlCityRepository::TABLE_NAME)
            ->andWhere([$attribute => $value])
            ->one();
        if ($row) {
            return $this->toEntity($row);
        } else {
            return null;
        }
    }

    /**
     * @param $id
     * @return bool
     */
    public function existsById(string $id) : bool
    {
        $query = new Query();
        return $query->from(YiiSqlCityRepository::TABLE_NAME)
            ->where(['id' => $id])
            ->exists();
    }

    /**
     * @param array $entities
     * @return array
     */
    public function toIdAndNameArray(array $entities) : array
    {
        /** @var $entity City*/
        $array = [];
        foreach($entities as $entity) {
            $array[$entity->getId()] = $entity->getName();
        }
        return $array;
    }

    /**
     * @param $result
     * @return City
     */
    private function toEntity(array $result) : City
    {
        return $this->cityHydrator->hydrate($result);
    }
}